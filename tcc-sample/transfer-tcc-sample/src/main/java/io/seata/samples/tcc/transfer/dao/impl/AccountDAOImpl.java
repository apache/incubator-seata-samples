/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.tcc.transfer.dao.impl;

import java.sql.SQLException;

import io.seata.samples.tcc.transfer.dao.AccountDAO;
import io.seata.samples.tcc.transfer.domains.Account;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 余额账户 DAO 实现
 */
public class AccountDAOImpl implements AccountDAO {

    public SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public void addAccount(Account account) throws SQLException {
        sqlSession.insert("addAccount", account);
    }

    @Override
    public int updateAmount(Account account) throws SQLException {
        return sqlSession.update("updateAmount", account);
    }

    @Override
    public Account getAccount(String accountNo) throws SQLException {
        return (Account)sqlSession.selectOne("getAccount", accountNo);
    }

    @Override
    public Account getAccountForUpdate(String accountNo) throws SQLException {
        return (Account)sqlSession.selectOne("getAccountForUpdate", accountNo);
    }

    @Override
    public int updateFreezedAmount(Account account) throws SQLException {
        return sqlSession.update("updateFreezedAmount", account);
    }

    @Override
    public void deleteAllAccount() throws SQLException {
        sqlSession.update("deleteAllAccount");
    }

}
