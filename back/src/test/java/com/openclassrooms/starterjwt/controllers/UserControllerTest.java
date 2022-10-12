package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
	@Test
	@WithMockUser
	public void testFindById() throws Exception {
		mockMvc.perform(get("/api/user/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", is("yoga2@studio.com")));
	}
	
	@Test
	@WithMockUser
	public void testNotFoundFindById() throws Exception {
		mockMvc.perform(get("/api/user/100"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void testNotAuthFindById() throws Exception {
		mockMvc.perform(get("/api/user/1"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser
	public void testNumberFormatFindById() throws Exception {
		mockMvc.perform(get("/api/user/toto"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "user@studio.com")
	public void testSave() throws Exception {
		mockMvc.perform(delete("/api/user/3"))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "yoga3@studio.com")
	public void testSaveNotFound() throws Exception {
		mockMvc.perform(delete("/api/user/100"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(username = "toto@toto.com")
	public void testNotAuthSave() throws Exception {
		mockMvc.perform(delete("/api/user/1"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser(username = "toto@toto.com")
	public void testNumberFormatSave() throws Exception {
		mockMvc.perform(delete("/api/user/toto"))
		.andExpect(status().isBadRequest());
	}

}
