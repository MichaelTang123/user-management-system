package com.michaeltang.usermanagement.test.ut;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaeltang.usermanagement.RestApiApplication;
import com.michaeltang.usermanagement.controller.UserController;
import com.michaeltang.usermanagement.exception.CommonExceptionHandler;
import com.michaeltang.usermanagement.common.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RestApiApplication.class, CommonExceptionHandler.class})
@TestPropertySource(properties = {
	"api.exception.message=error"})
public class UserControllerTest {
    private static final String API_ENDPOINT_PATH_USERS = "/users";

    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(
                		CommonExceptionHandler.class)
                .build();
    }

    @Test
    public void testListUsers() throws Exception {
    	registerUser("usr2", "fistname", "lastname", "usr2@example.com");
    	registerUser("usr1", "fistname", "lastname", "usr1@example.com");
    	
    	final MvcResult mvcResult = mockMvc
                .perform(get(API_ENDPOINT_PATH_USERS).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value("usr1"))
                .andExpect(jsonPath("$.content[0].email").value("usr1@example.com"))
                .andExpect(jsonPath("$.content[0].firstName").value("fistname"))
                .andExpect(jsonPath("$.content[0].lastName").value("lastname"))
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1));
                .andExpect(jsonPath("$.content[1].id").value("usr2"))
                .andExpect(jsonPath("$.content[1].email").value("usr2@example.com"))
                .andExpect(jsonPath("$.content[1].firstName").value("fistname"))
                .andExpect(jsonPath("$.content[1].lastName").value("lastname"));
    }
    
    
    @Test
    public void testRegisterUsers() throws Exception {
    	final User user = createUser("user1", "fistname", "lastname", "user1@example.com");
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncStarted())
        	  .andReturn();
        mockMvc
	        .perform(asyncDispatch(mvcResult))
	        .andExpect(status().isCreated())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value("user1"))
            .andExpect(jsonPath("$.email").value("user1@example.com"))
            .andExpect(jsonPath("$.firstName").value("fistname"))
            .andExpect(jsonPath("$.lastName").value("lastname"))
            .andExpect(jsonPath("$.createDate").isNotEmpty())
            .andExpect(jsonPath("$.updateDate").isNotEmpty());
    }
    
    @Test
    public void testRegisterUsersWithInvalidUserId() throws Exception {
        final User user = createUser("invaliduser**", "fistname", "lastname", "user1@example.com");
        mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncNotStarted())
        	  .andExpect(status().isBadRequest())
        	  .andReturn();
    }
    
    @Test
    public void testRegisterUsersWithInvalidEmail() throws Exception {
    	final User user = createUser("user2", "fistname", "lastname", "invalid email");
        mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncNotStarted())
        	  .andExpect(status().isBadRequest())
        	  .andReturn();
    }
    
    @Test
    public void testRegisterUsersWithInvalidFirstName() throws Exception {
    	final User user = createUser("user2", "---?", "lastname", "user2@example.com");
        mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncNotStarted())
        	  .andExpect(status().isBadRequest())
        	  .andReturn();
    }
    
    @Test
    public void testRegisterUsersWithInvalidLastName() throws Exception {
    	final User user = createUser("user2", "fistname", "---!", "user2@example.com");
        mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncNotStarted())
        	  .andExpect(status().isBadRequest())
        	  .andReturn();
    }
    
    @Test
    public void testRegisterUsersWithDuplicateId() throws Exception {
    	registerUser("user3", "fistname", "lastname", "user3@example.com");
    	User user = createUser("user3", "fistname", "lastname", "user3@example.com");
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncStarted())
        	  .andReturn();
        mockMvc
	        .perform(asyncDispatch(mvcResult))
	        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testRegisterUsersWithDuplicateEmail() throws Exception {
    	registerUser("user4", "fistname", "lastname", "user4@example.com");
    	User user = createUser("user5", "fistname", "lastname", "user4@example.com");
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncStarted())
        	  .andReturn();
        mockMvc
	        .perform(asyncDispatch(mvcResult))
	        .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUser() throws Exception {
    	registerUser("user6", "fistname", "lastname", "user6@example.com");

        final MockHttpServletRequestBuilder getRequest = get(API_ENDPOINT_PATH_USERS + "/user6")
                .accept(MediaType.APPLICATION_JSON);

        final MvcResult mvcResult = mockMvc
                .perform(getRequest)
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("user6"))
                .andExpect(jsonPath("$.firstName").value("fistname"))
                .andExpect(jsonPath("$.lastName").value("lastname"))
                .andExpect(jsonPath("$.email").value("user6@example.com"));
    }
    
    @Test
    public void testEditUser() throws Exception {
    	final User user = registerUser("user7", "fistname", "lastname", "user7@example.com");

    	final MockHttpServletRequestBuilder getRequest = get(API_ENDPOINT_PATH_USERS + "/user7")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc
                .perform(getRequest)
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("user7"))
                .andExpect(jsonPath("$.firstName").value("fistname"))
                .andExpect(jsonPath("$.lastName").value("lastname"))
                .andExpect(jsonPath("$.email").value("user7@example.com"));

        user.setEmail("user72@example.com");
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
        	      .put(API_ENDPOINT_PATH_USERS + "/user7")
        	      .content(obj2string(user))
        	      .contentType(MediaType.APPLICATION_JSON)
        	      .accept(MediaType.APPLICATION_JSON))
          	  .andExpect(MockMvcResultMatchers.request().asyncStarted())
          	  .andReturn();
          mockMvc
  	        .perform(asyncDispatch(mvcResult))
  	        .andExpect(status().isNoContent());
          
          mvcResult = mockMvc
                  .perform(getRequest)
                  .andExpect(MockMvcResultMatchers.request().asyncStarted())
                  .andReturn();
          mockMvc
                  .perform(asyncDispatch(mvcResult))
                  .andExpect(status().isOk())
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andExpect(jsonPath("$.id").value("user7"))
                  .andExpect(jsonPath("$.firstName").value("fistname"))
                  .andExpect(jsonPath("$.lastName").value("lastname"))
                  .andExpect(jsonPath("$.email").value("user72@example.com"));
    }
    
    @Test
    public void testDeleteExistUsers() throws Exception {
    	registerUser("user8", "fistname", "lastname", "user8@example.com");
    	registerUser("user9", "fistname", "lastname", "user9@example.com");
    	registerUser("user10", "fistname", "lastname", "user10@example.com");

        final MockHttpServletRequestBuilder deleteRequest = delete(API_ENDPOINT_PATH_USERS + "?ids=user8,user9,user10");
        MvcResult mvcResult = mockMvc
                .perform(deleteRequest)
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
        
        final MockHttpServletRequestBuilder getRequest = get(API_ENDPOINT_PATH_USERS + "/user8")
                .accept(MediaType.APPLICATION_JSON);
        mvcResult = mockMvc
                .perform(getRequest)
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testDeleteNotExistUsers() throws Exception {
        final MockHttpServletRequestBuilder deleteRequest = delete(API_ENDPOINT_PATH_USERS + "?ids=notexist");
        MvcResult mvcResult = mockMvc
                .perform(deleteRequest)
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }
    
    
    private User registerUser(final String userId, final String fistName, final String lastName, final String email)
    		throws Exception {
        final User user = createUser(userId, fistName, lastName, email);
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      	      .post(API_ENDPOINT_PATH_USERS)
      	      .content(obj2string(user))
      	      .contentType(MediaType.APPLICATION_JSON)
      	      .accept(MediaType.APPLICATION_JSON))
        	  .andExpect(MockMvcResultMatchers.request().asyncStarted())
        	  .andReturn();
        mockMvc
	        .perform(asyncDispatch(mvcResult))
	        .andExpect(status().isCreated());
        return user;
    }
    
    private User createUser(String userId, String fistName, String lastName, String email) {
        final User user = new User();
        user.setId(userId);
        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }
    
    private String obj2string(final Object obj) throws JsonProcessingException {
    	return new ObjectMapper().writeValueAsString(obj);
    }
}
