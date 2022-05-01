package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController // needed to allow the class to be recognized as a controller;sends and receives data
public class AccountController {

    @Autowired //find accountDao object
    AccountDao accountDao;

    @Autowired
    UserDao userDao;
//neeed a post mapping to update the current  blance so it desnt reset

    @Autowired
    TransferDao transferDao;

    @RequestMapping(path ="/transfer", method = RequestMethod.POST)
    public Transfer makeTransfer(@RequestBody Transfer transfer){
        //System.out.println("DEBUG, what the object looks like:");
        return transferDao.updateTransfer(transfer);
    }

    @RequestMapping(path="/user", method = RequestMethod.GET)
    public List <User> getUsers() {
        return userDao.findAll();
    }


    @RequestMapping(path="/transfer/{id}", method = RequestMethod.GET)
    public List <Transfer> listTransfers(@PathVariable int id) {
        // System.out.println("DEBUG, what the object looks like: ");
        return transferDao.getAllTransfersByUserId(id);
    }

    @RequestMapping(path="/transfer_status/{transferId}", method = RequestMethod.GET)
    public List <Transfer> listTransfersByTransferId(@PathVariable int transferId) {
        // System.out.println("DEBUG, what the object looks like: ");
        return transferDao.getAllTransfersById(transferId);
    }


        @RequestMapping(path="/account", method = RequestMethod.GET)
    public BigDecimal getAccount(Principal principal) {
        return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path = "/whoisthis" , method = RequestMethod.GET)
    public String whoisthis(Principal principal) {
        return principal.getName();
    }

        }

