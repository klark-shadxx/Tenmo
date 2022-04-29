package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
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

private Transfer transferObjectMapper(SqlRowSet results){

    Transfer transfer = new Transfer();
    transfer.setTransferId(results.getInt("transfer_id"));
    transfer.setTransferTypeId(results.getInt("transfer_type_id"));
    transfer.setTransferStatusId(results.getInt("transfer_status_id"));
    transfer.setAccountFrom(results.getInt("account_from"));//this is account ID
    transfer.setAccountTo(results.getInt("account_to"));//this is account ID
    transfer.setAmount(results.getBigDecimal("amount"));

    return transfer;
}


    @Override
    public List<Transfer> getAllTransfersById(int transferId) {// return all info from transfer and transfer_type table for all transactions
        String sql = "SELECT*FROM transfer WHERE transfer_id = ? ";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, transferId);
        List<Transfer> transfers = new ArrayList<>();
        while(results.next()){
            transfers.add(transferObjectMapper(results));
        }
        return transfers;
    }

    @Override
    public Transfer getATransferById(int id) {// all info from transfer and transfer_type table for 1 transaction
        String sql = "SELECT*FROM transfer WHERE transfer_id = ? ";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, id);

        Transfer transfer = null;
        if(results.next())
        {
            transfer=transferObjectMapper(results);
        }

        return transfer;

    }

    @Override
    public Transfer updateTransfer(Transfer transfer) {
        String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
        jdbcTemplate.update(sql, BigDecimal.class, transfer);
        String sql2 = "UPDATE account SET balance = balance + ? WHERE user_id= ?";
        jdbcTemplate.update(sql, BigDecimal.class, transfer);
        String sql3 = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?,?)";
           jdbcTemplate.update(sql3, Integer.class,transfer.getTransferId(), transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo());
            return transfer;
    }
}
    // do I have to do a return and make a  int newTransferId =  for a new transferid
