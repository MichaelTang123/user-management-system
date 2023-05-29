package com.michaeltang.usermanagement.test.ut;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.michaeltang.usermanagement.RestApiApplication;
import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.RegistrationEvent;
import com.michaeltang.usermanagement.common.model.User;
import com.michaeltang.usermanagement.controller.EventController;
import com.michaeltang.usermanagement.exception.CommonExceptionHandler;
import com.michaeltang.usermanagement.repositories.RegEventRepository;
import com.michaeltang.usermanagement.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
		RestApiApplication.class, CommonExceptionHandler.class})
@TestPropertySource(properties = {
	"api.exception.message=error"})
public class EventControllerTest {
	private static final String API_ENDPOINT_PATH_EVENTS = "/events";

    @Autowired
    private EventController eventController;

    private MockMvc mockMvc;
    
    @Autowired
    private RegEventRepository regEventRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .setControllerAdvice(
                		CommonExceptionHandler.class)
                .build();
        createRegEvent("usr01", "fistname", "lastname", "usr01@example.com");
    }
    
    @Test
    public void testListEvents() throws Exception {
    	
    	final MvcResult mvcResult = mockMvc
                .perform(get(API_ENDPOINT_PATH_EVENTS).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].user").value("usr01"))
                .andExpect(jsonPath("$.content[0].email").value("usr01@example.com"))
                .andExpect(jsonPath("$.content[0].emailState").value("pending"));
    }
    
    private RegistrationEvent createRegEvent(String userId, String fistName, String lastName, String email) {
    	final User user = createUser(userId, fistName, lastName, email);
		final RegistrationEvent event = new RegistrationEvent();
		event.setUser(user);
		event.setEmailState(Common.EMAIL_PENDING);
		event.setCreateDate(user.getCreateDate());
		event.setUpdateDate(user.getUpdateDate());
		regEventRepository.save(event);
		return event;
	}
    
    private User createUser(String userId, String fistName, String lastName, String email) {
        final User user = new User();
        user.setId(userId);
        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setEmail(email);
        final Date now = new Date(System.currentTimeMillis());
        user.setCreateDate(now);
    	user.setUpdateDate(now);
    	return userRepository.save(user);
    }
}
