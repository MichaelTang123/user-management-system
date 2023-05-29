package com.michaeltang.usermanagement.test.ut;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.michaeltang.usermanagement.RestApiApplication;
import com.michaeltang.usermanagement.converter.Converter;
import com.michaeltang.usermanagement.common.exception.ValidationException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApiApplication.class)
@TestPropertySource(properties = {
	"api.exception.message=error"})
public class ConverterTest {
	@Autowired
    @Qualifier("idListConverter")
    private Converter idListConverter;
	
	@Test
    public void testConvertSuc() throws Exception {
		List<String> res = (List<String>) idListConverter.convert("user0");
		assertEquals("Unexpected user list", 1, res.size());
		assertEquals("Unexpected user list", "user0", res.get(0));
		
		res = (List<String>) idListConverter.convert("user1,user2");
		assertEquals("Unexpected user list", 2, res.size());
		assertEquals("Unexpected user list", "user1", res.get(0));
		assertEquals("Unexpected user list", "user2", res.get(1));
    }
	
	@Test
    public void testConvertTrim() throws Exception {
		List<String> res = (List<String>) idListConverter.convert(" user1,user2 ");
		assertEquals("Unexpected user list", 2, res.size());
		assertEquals("Unexpected user list", "user1", res.get(0));
		assertEquals("Unexpected user list", "user2", res.get(1));
		
		res = (List<String>) idListConverter.convert("user1, user2");
		assertEquals("Unexpected user list", 2, res.size());
		assertEquals("Unexpected user list", "user1", res.get(0));
		assertEquals("Unexpected user list", "user2", res.get(1));
    }
	
	@Test(expected = ValidationException.class)
    public void testConvertInvalidId() throws Exception {
		idListConverter.convert(" user1&user2 ");
    }
	
	@Test
    public void testConvertSizeLimitExceeded() throws Exception {
		List<String> res = (List<String>) idListConverter.convert("user1,user2,user3,user4,user5"
				+ ",user6,user7,user8,user9,user10,user11");
		assertEquals("Unexpected user list", 10, res.size());
    }
}
