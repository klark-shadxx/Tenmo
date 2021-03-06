package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();
    private AuthenticatedUser currentUser;

    //   2
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {

                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        accountService.setAuthToken(currentUser.getToken());
        userService.setAuthToken(currentUser.getToken());
        if (currentUser == null) {
            //need to figure out how to handle if the password is incorrect
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        String menuSelection2 = " ";
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                printAllUsers();
                sendBucks();

            } else if (menuSelection == 5) {
                printAllUsers();
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance();
        System.out.println("Your current balance is: $" + balance);

    }

    private void viewTransferHistory() {
        printAllTranfers();
        String recipientIdStr = consoleService.promptForString("Do you w: ");
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        String recipientIdStr = consoleService.promptForString("Who do you want to send money to (Please enter user id) ?: ");
        long temp = currentUser.getUser().getId();
        int recipientId = Integer.parseInt(recipientIdStr);
        if (recipientIdStr.equals(String.valueOf(temp))) {
            consoleService.printCannotSendMoneyToSelf();
        } else {
            if (!userService.doesUserExist(recipientId)) {
                consoleService.printUserDoesNotExistError();
            } else {

                BigDecimal moneyInput = consoleService.promptForBigDecimal("How much would you like to send?: ");
                if (moneyInput.compareTo(accountService.getBalance()) > 0 || moneyInput.compareTo(new BigDecimal("0.00")) <= 0) {
                    consoleService.printYouDoNotHaveEnoughMoney();
                } else {
                    Transfer transfer = new Transfer();

                    int accountFrom = (int) temp;

                    transfer.setAccountFrom(accountFrom);
                    transfer.setAccountTo(recipientId);
                    transfer.setAmount(moneyInput);
                    transfer.setTransferTypeId(2);
                    transfer.setTransferStatusId(2);
                    accountService.makeTransfer(transfer);


                }


            }


            //String amountToSend = scanner.nextLine();


            //BigDecimal balance = accountService.getBalance().subtract(moneyInput);
            //System.out.println(balance);


            //if the user entered doesnt exists  enter an error, and make them enter another user
            //if user does exist, prompt for the amount of money

            //

            //once you enter the amount of money,
            //if you dont have enough money, give an error
            //if you do have enough money, deduct that from your initial balance
//        User username = userService.getUser();
//        System.out.println(username);

            // TODO Auto-generated method stub

        }
    }

    private void requestBucks() {
//        String recipientIdStr = consoleService.promptForString("Who do you want to request money from  (Please enter user id) ?: ");
//        long temp = currentUser.getUser().getId();
//        int recipientId = Integer.parseInt(recipientIdStr);
//        if (recipientIdStr == String.valueOf(temp)) {
//            consoleService.print();
//        } else {
//            if (!userService.doesUserExist(recipientId)) {
//                consoleService.printUserDoesNotExistError();
//            } else {
//
//                BigDecimal moneyInput = consoleService.promptForBigDecimal("How much would you like to send?: ");
//                if (moneyInput.compareTo(accountService.getBalance()) > 0 || moneyInput.compareTo(new BigDecimal("0.00")) <= 0) {
//                    consoleService.printCannotSendMoneyToSelf();
//                } else {
//                    Transfer transfer = new Transfer();
//
//                    int accountFrom = (int) temp;
//
//                    transfer.setAccountFrom(accountFrom);
//                    transfer.setAccountTo(recipientId);
//                    transfer.setAmount(moneyInput);
//                    transfer.setTransferTypeId(2);
//                    transfer.setTransferStatusId(2);
//                    accountService.makeTransfer(transfer);
//
//
//                }
//
//
//            }
//
//        }
    }

    private void printAllUsers() {
        User[] users = userService.getAllUser();
        for (User user : users) {
            System.out.println("___________________" + "\nUserID        Name");
            System.out.println(user.getId() + "          " + user.getUsername());

        }
    }

    private void printAllTranfers() {

        Transfer[] transfers = accountService.getAllTransfersByUserId(currentUser.getUser().getId());
        for (Transfer transfer : transfers) {
            System.out.println(transfer);
        }
    }
}




