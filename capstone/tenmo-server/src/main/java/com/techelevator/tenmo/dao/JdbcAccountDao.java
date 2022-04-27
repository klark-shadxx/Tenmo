package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component //
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){this.jdbcTemplate =jdbcTemplate;}

    @Override
    // need to use Principal to access user_id without displaying the id
    public Account getBalance(Long id) {
        String sql = "SELECT user_id, balance FROM account WHERE user_id = ?;";// sql matches the mapper results
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, id);

        Account account =null;
        if (results.next()){
            account = mapAccountMapper(results);
        }
        return account;

    }
    private Account mapAccountMapper(SqlRowSet rs){
        Account account = new Account();
        account.setId(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;

    }
}
