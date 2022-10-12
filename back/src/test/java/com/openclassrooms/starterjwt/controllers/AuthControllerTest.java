package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	public MockMvc mockMvc;
	
	@Test
	public void testAuthenticateUserAdmin() throws Exception {		
				mockMvc.perform(post("/api/auth/login")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"email\": \"yoga2@studio.com\", \"password\": \"test!1234\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("yoga2@studio.com")))
				.andExpect(jsonPath("$.admin", is(true)));
	}
	
	@Test
	public void testAuthenticateUserAndAnotherRequest() throws Exception {		
		MvcResult result = mockMvc.perform(post("/api/auth/login")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"email\": \"yoga2@studio.com\", \"password\": \"test!1234\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("yoga2@studio.com")))
				.andExpect(jsonPath("$.admin", is(true)))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		String token = JsonPath.parse(response).read("$.token");
		
		mockMvc.perform(get("/api/teacher").header("Authorization", "Bearer " + token))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testAuthenticateUserNotAdmin() throws Exception {		
				mockMvc.perform(post("/api/auth/login")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"email\": \"user@studio.com\", \"password\": \"test!1234\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("user@studio.com")))
				.andExpect(jsonPath("$.admin", is(false)));	
	}
	
	@Test
	public void testAuthenticateUserBadCredentials() throws Exception {		
				mockMvc.perform(post("/api/auth/login")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"email\": \"toto@studio.com\", \"password\": \"test!1234\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testRegister() throws Exception {
		mockMvc.perform(post("/api/auth/register")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"lastName\": \"toto\",\"firstName\": \"toto\",\"email\": \"romain@studio.com\",\"password\": \"test!1234\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("User registered successfully!")))				
				.andExpect(status().isOk());
	}
	
	@Test
	public void testRegisterAlreadyExist() throws Exception {
		mockMvc.perform(post("/api/auth/register")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"lastName\": \"Admin\",\"firstName\": \"Admin\",\"email\": \"yoga2@studio.com\",\"password\": \"test!1234\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.message", is("Error: Email is already taken!")))
				.andExpect(status().isBadRequest());		
	}
		
}
