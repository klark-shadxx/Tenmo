package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

public class UserService {
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
    private HttpEntity<Void> makeAuthEntity(){

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return entity;// entity is the token added to the headers,
        // we use this to access the restricted requests in the AppController
    }


    public User[] getAllUser() {

        User [] listOfUsersNames = null;
    try {

    listOfUsersNames = restTemplate.exchange(// performs the request
            API_BASE_URL + "user", //need url
            HttpMethod.GET, // request type is a GET; Http is an enum method
            makeAuthEntity(),// header that contains token to access server
            User[].class).getBody(); // what object do I want to deserialize to?


} catch (RestClientResponseException | ResourceAccessException e){
    System.out.println("Something went awry");
}


        return listOfUsersNames;
    }

}
