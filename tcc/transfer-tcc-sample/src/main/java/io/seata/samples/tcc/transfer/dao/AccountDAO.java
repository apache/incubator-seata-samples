package io.seata.samples.tcc.transfer.dao;


import io.seata.samples.tcc.transfer.domains.Account;

import java.sql.SQLException;

/**
 * 余额账户 DAO
 */
public interface AccountDAO {

    void addAccount(Account account) throws SQLException;
    
    int updateAmount(Account account) throws SQLException;
    
    int updateFreezedAmount(Account account) throws SQLException;
    
    Account getAccount(String accountNo) throws SQLException;
    
    Account getAccountForUpdate(String accountNo) throws SQLException;
    
    void deleteAllAccount() throws SQLException;
}
