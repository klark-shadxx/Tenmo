package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    //private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(DataSource ds) {this.jdbcTemplate =new JdbcTemplate(ds);}
@Autowired
AccountDao accountDao;

    private Transfer transferTypeObjectMapper(SqlRowSet results){

        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeDescription(results.getString("transfer_type_desc"));
        transfer.setTransferStatusDescription(results.getString("transfer_status_desc"));
        transfer.setAccountFrom(results.getInt("account_from"));//this is account ID
        transfer.setAccountTo(results.getInt("account_to"));//this is account ID
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
    private Transfer transferObjectMapper(SqlRowSet results){

        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setAccountFrom(results.getInt("account_from"));//this is account ID
        transfer.setAccountTo(results.getInt("account_to"));//this is account ID
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));

        return transfer;
    }



    @Override
    public List<Transfer> getAllTransfersById(int transferId) {// return all info from transfer and transfer_type table for all transactions
        String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN transfer_type On transfer_type.transfer_type_id = transfer.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, transferId);
        List<Transfer> transfers = new ArrayList<>();
        while(results.next()){
            transfers.add(transferTypeObjectMapper(results));
        }
        return transfers;
    }

    @Override
    public Transfer getATransferById(int transferId) {// all info from transfer and transfer_type table for 1 transaction
        String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN transfer_type On transfer_type.transfer_type_id = transfer.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, transferId);

        Transfer transfer = null;
        if(results.next())
        {
            transfer=transferTypeObjectMapper(results);
        }

        return transfer;

    }

    @Override
    public Transfer updateTransfer(Transfer transfer) {

        // Take transfer.getAccountFrom() and transform this into an account number
         int FROM = accountDao.getAccountByUserId(transfer.getAccountFrom());
        // Take transfer.getAccountTo() and transform this into an account number
        int TO = accountDao.getAccountByUserId(transfer.getAccountTo());
        String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFrom());
        String sql2 = "UPDATE account SET balance = balance + ? WHERE user_id= ?";
        jdbcTemplate.update(sql2,transfer.getAmount(), transfer.getAccountTo()); //not happy
        String sql3 = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?)";
           jdbcTemplate.update(sql3, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    FROM, TO, transfer.getAmount());
            return transfer;
    }

    @Override
    public List<Transfer> getAllTransfersByUserId(int userId) {// return all info from transfer and transfer_type table for all transactions
        String sql = "SELECT transfer_id, transfer_type.transfer_type_id, transfer_type_desc, transfer.transfer_status_id, transfer_status_desc, account_from, account_to, amount, balance" +
                " FROM transfer " +
                "JOIN account ON transfer.account_from = account.account_id " +
                "JOIN transfer_type On transfer_type.transfer_type_id = transfer.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE user_id = ? ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        List<Transfer> transfers = new ArrayList<>();
        while(results.next()){
            transfers.add(transferTypeObjectMapper(results));
        }
        return transfers;
    }

    @Override
    public List<Transfer> findAll() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, account_from, account_to, amount " +
                "FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Transfer transfer = transferObjectMapper(results);
            transfers.add(transfer);
        }
        return transfers;
    }

}
    // do I have to do a return and make a  int newTransferId =  for a new transferid
