package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    //RestTemplate performs GET, POST, PUT, and DELETE requests.

    private String authToken = null; //authToken is where we store the token that was generated from the login endpoint.

    //when you log in, you auto-populated authToken
    // the token goes to the header of the request= Authorization Bearer tokenValue
    // you need a header to include the token, and to send a header to the server you need an entity.
    // The entity is included in the PODT request
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Creates a new HttpEntity with the `Authorization: Bearer:` header and a reservation request body
     */


    /**
     * Returns an HttpEntity with the `Authorization: Bearer:` header
     */
    // HttpEntity methods return void because it does not have a body
    //This method makes a call to the server to get a list of flights
    //We want to consume the request for /flights in AppController
    private HttpEntity<Void> makeAuthEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return entity;// entity is the token added to the headers,
        // we use this to access the restricted requests in the AppController
    }
    // we want to capture the info we get back from the server after the request is returned
    // we are getting back a list of objects that are being serialized
    // regular data types can't do this
    //we need to mirror the server and the client by making another flight class in client to match the one in server
    // the server will send us back info that needs to be turned into an object (deserialized) java can read.
    // we can store this called info into a class

    public BigDecimal getBalance() {

        BigDecimal balance = restTemplate.exchange(// performs the request
                API_BASE_URL + "account", //need url
                HttpMethod.GET, // request type is a GET; Http is an enum method
                makeAuthEntity(),// header that contains token to access server
                BigDecimal.class).getBody(); // what object do I want to deserialize to?

        return balance;
    }

//new!
    public boolean update(Account updatedBalance) {
        HttpEntity<Account> entity = makeEntity(updatedBalance);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + updatedBalance.getId(), entity);
            success = true;
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Account> makeEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(account, headers);
    }
}
// .exchange= does not return the whole body of info we want, just the
//response entity, so we add .getBody to add all the info


