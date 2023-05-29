package com.michaeltang.usermanagement.test.bdd.stepdefs;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpHeaders;

import com.michaeltang.usermanagement.RestApiApplication;
import com.michaeltang.usermanagement.common.model.User;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RestApiApplication.class})
@TestPropertySource(properties = {
	"api.exception.message=error"})
public class UserSteps {
	@Given("the test user {string} not exists")
    public void the_test_user_not_exists(String user) throws Throwable {
		deleteUser(user);
    }
	
	@When("the client register new user {string}")
    public void the_client_register_new_user(String user) throws Throwable {
		registerUser(user, user, user, user + "@email.com");
    }

	@Then("the client should be able to query the user {string}")
    public void the_client_should_be_able_to_query_the_user(String user) throws Throwable {
		final User res = queryUser(user);
		assertNotNull(res);
    }

	@Then("the client should be able to see the registration event")
    public void the_client_should_be_able_to_see_the_registration_event() throws Throwable {
    }
    
    @Autowired
    private TestRestTemplate testRestTemplate; 

    private void registerUser(String userId, String fistName, String lastName, String email) throws IOException {
    	final User user = createUser(userId, fistName, lastName, email);
    	MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    	map.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    	map.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    	HttpEntity<User> request = new HttpEntity<>(user, map);
    	ResponseEntity<User> res = testRestTemplate.postForEntity(testRestTemplate.getRootUri() + "/apis/users", request, User.class);
    	assertNotNull(res);
    }
    
    private void deleteUser(String userId) throws IOException {
    	testRestTemplate.delete(testRestTemplate.getRootUri() + "/apis/users/" + userId);
    }
    
    private User queryUser(String userId) throws IOException {
    	return testRestTemplate.getForObject(testRestTemplate.getRootUri() + "/apis/users/" + userId, User.class);
    }
    
    private User createUser(String userId, String fistName, String lastName, String email) {
        final User user = new User();
        user.setId(userId);
        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }
}
