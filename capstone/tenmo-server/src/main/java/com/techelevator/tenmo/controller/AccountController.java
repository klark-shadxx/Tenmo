package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController // needed to allow the class to be recognized as a controller
public class AccountController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @RequestMapping(path="/account", method = RequestMethod.GET)
    public BigDecimal getAccount(Principal principal) {
        return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path = "/whoisthis" , method = RequestMethod.GET)
    public String whoisthis(Principal principal) {
        return principal.getName();
    }

        }

