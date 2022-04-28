package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component //
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){this.jdbcTemplate =jdbcTemplate;}

    @Override
    //1.
    // need to use Principal to access user_id without displaying the id
    public BigDecimal getBalance(String username) {
        String sql = "SELECT account.balance FROM account join tenmo_user on tenmo_user.user_id = account.user_id " +
                "WHERE username = ?;";// sql matches the mapper results
        return this.jdbcTemplate.queryForObject(sql, BigDecimal.class, username);

    }
    //public List<Transfers>


    private Account mapAccountMapper(SqlRowSet rs){
        Account account = new Account();
        account.setId(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;

    }
}
