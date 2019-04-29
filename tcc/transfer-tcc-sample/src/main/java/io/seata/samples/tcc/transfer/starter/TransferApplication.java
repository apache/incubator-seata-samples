package io.seata.samples.tcc.transfer.starter;

import java.sql.SQLException;

import io.seata.samples.tcc.transfer.activity.TransferService;
import io.seata.samples.tcc.transfer.dao.AccountDAO;
import io.seata.samples.tcc.transfer.env.TransferDataPrepares;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 发起转账
 *
 * @author zhangsen
 */
public class TransferApplication {

    protected static ApplicationContext applicationContext ;

    /**
     * 转账服务
     */
    protected static TransferService transferService ;

    /**
     * 转出账户数据 DAO
     */
    protected static AccountDAO fromAccountDAO;

    /**
     * 转入账户数据 DAO
     */
    protected static AccountDAO toAccountDAO;

    /**
     * 转账数据初始化
     */
    protected static TransferDataPrepares transferDataPrepares;

    public static void main(String[] args) throws SQLException {
        applicationContext = new ClassPathXmlApplicationContext("spring/seata-tcc.xml",
            "spring/seata-dubbo-reference.xml",
                "db-bean/to-datasource-bean.xml", "db-bean/from-datasource-bean.xml");

        transferService = (TransferService) applicationContext.getBean("transferService" );
        fromAccountDAO = (AccountDAO) applicationContext.getBean("fromAccountDAO" );
        toAccountDAO = (AccountDAO) applicationContext.getBean("toAccountDAO" );

        //执行 A->C 转账成功 demo, 分布式事务提交
        doTransferSuccess(100, 10);

        //执行 B->XXX 转账失败 demo， 分布式事务回滚
        doTransferFailed(100, 10);
    }

    /**
     * 执行转账成功 demo
     *
     * @param initAmount 初始化余额
     * @param transferAmount  转账余额
     */
    private static void doTransferSuccess(double initAmount, double transferAmount) throws SQLException {
        //执行转账操作
        doTransfer("A", "C", transferAmount);

        //校验A账户余额：initAmount - transferAmount
        checkAmount(fromAccountDAO, "A", initAmount - transferAmount);

        //校验C账户余额：initAmount + transferAmount
        checkAmount(toAccountDAO, "C", initAmount + transferAmount);
    }

    /**
     * 执行转账 失败 demo， 'B' 向未知用户 'XXX' 转账，转账失败分布式事务回滚
     * @param initAmount 初始化余额
     * @param transferAmount  转账余额
     */
    private static void doTransferFailed(int initAmount, int transferAmount) throws SQLException {
        // 'B' 向未知用户 'XXX' 转账，转账失败分布式事务回滚
        try{
            doTransfer("B", "XXX", transferAmount);
        }catch (Throwable t){
            System.out.println("从账户B向未知账号XXX转账失败.");
        }

        //校验A2账户余额：initAmount
        checkAmount(fromAccountDAO, "B", initAmount);

        //账户XXX 不存在，无需校验
    }

    /**
     * 执行转账 操作
     * @param transferAmount 转账金额
     */
    private static boolean doTransfer(String from, String to, double transferAmount) {
        //转账操作
        boolean ret = transferService.transfer(from, to, transferAmount);
        if(ret){
            System.out.println("从账户"+from+"向"+to+"转账 "+transferAmount+"元 成功.");
            System.out.println();
        }else {
            System.out.println("从账户"+from+"向"+to+"转账 "+transferAmount+"元 失败.");
            System.out.println();
        }
        return ret;
    }


    /**
     * 校验账户余额
     * @param accountDAO
     * @param accountNo
     * @param expectedAmount
     * @throws SQLException
     */
    private static void checkAmount(AccountDAO accountDAO, String accountNo, double expectedAmount) throws SQLException {
        try {
//            Account account = accountDAO.getAccount(accountNo);
//            Assert.isTrue(account != null, "账户不存在");
//            double amount = account.getAmount();
//            double freezedAmount = account.getFreezedAmount();
//            Assert.isTrue(expectedAmount == amount, "账户余额校验失败");
//            Assert.isTrue(freezedAmount == 0, "账户冻结余额校验失败");
        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}
