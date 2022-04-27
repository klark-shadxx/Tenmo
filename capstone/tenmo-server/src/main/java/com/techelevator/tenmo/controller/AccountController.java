package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController // needed to allow the class to be recognized as a controller
public class AccountController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @RequestMapping(path="/account/{id}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable Long id) {
        return accountDao.getBalance(id);
    }

        }

