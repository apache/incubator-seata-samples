package io.seata.samples.tcc.transfer.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.samples.tcc.transfer.action.SecondTccAction;
import io.seata.samples.tcc.transfer.dao.AccountDAO;
import io.seata.samples.tcc.transfer.domains.Account;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 加钱参与者实现
 *
 * @author zhangsen
 */
public class SecondTccActionImpl implements SecondTccAction {

    /**
     * 加钱账户 DAP
     */
    private AccountDAO toAccountDAO;

    private TransactionTemplate toDsTransactionTemplate;

    /**
     * 一阶段准备，转入资金 准备
     * @param businessActionContext
     * @param accountNo
     * @param amount
     * @return
     */
    @Override
    public boolean prepareAdd(final BusinessActionContext businessActionContext, final String accountNo, final double amount) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();

        return toDsTransactionTemplate.execute(new TransactionCallback<Boolean>(){

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    //校验账户
                    Account account = toAccountDAO.getAccountForUpdate(accountNo);
                    if(account == null){
                        System.out.println("prepareAdd: 账户["+accountNo+"]不存在, txId:" + businessActionContext.getXid());
                        return false;
                    }
                    //待转入资金作为 不可用金额
                    double freezedAmount = account.getFreezedAmount() + amount;
                    account.setFreezedAmount(freezedAmount);
                    toAccountDAO.updateFreezedAmount(account);
                    System.out.println(String.format("prepareAdd account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
                    return true;
                } catch (Throwable t) {
                    t.printStackTrace();
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }

    /**
     * 二阶段提交
     * @param businessActionContext
     * @return
     */
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        //账户ID
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        //转出金额
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        return toDsTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try{
                    Account account = toAccountDAO.getAccountForUpdate(accountNo);
                    //加钱
                    double newAmount = account.getAmount() + amount;
                    account.setAmount(newAmount);
                    //冻结金额 清除
                    account.setFreezedAmount(account.getFreezedAmount()  - amount);
                    toAccountDAO.updateAmount(account);

                    System.out.println(String.format("add account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
                    return true;
                }catch (Throwable t){
                    t.printStackTrace();
                    status.setRollbackOnly();
                    return false;
                }
            }
        });

    }

    /**
     * 二阶段回滚
     * @param businessActionContext
     * @return
     */
    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        //账户ID
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        //转出金额
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        return toDsTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try{
                    Account account = toAccountDAO.getAccountForUpdate(accountNo);
                    if(account == null){
                        //账户不存在, 无需回滚动作
                        return true;
                    }
                    //冻结金额 清除
                    account.setFreezedAmount(account.getFreezedAmount()  - amount);
                    toAccountDAO.updateFreezedAmount(account);

                    System.out.println(String.format("Undo prepareAdd account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
                    return true;
                }catch (Throwable t){
                    t.printStackTrace();
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }

    public void setToAccountDAO(AccountDAO toAccountDAO) {
        this.toAccountDAO = toAccountDAO;
    }

    public void setToDsTransactionTemplate(TransactionTemplate toDsTransactionTemplate) {
        this.toDsTransactionTemplate = toDsTransactionTemplate;
    }
}
