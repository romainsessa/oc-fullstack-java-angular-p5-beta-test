package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
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
public class TeacherControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
	@Test
	@WithMockUser
	public void testFindById() throws Exception {
		mockMvc.perform(get("/api/teacher/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.lastName", is("DELAHAYE")));
	}
	
	@Test
	@WithMockUser
	public void testNotFoundFindById() throws Exception {
		mockMvc.perform(get("/api/teacher/100"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void testNotAuthFindById() throws Exception {
		mockMvc.perform(get("/api/teacher/1"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser
	public void testNumberFormatFindById() throws Exception {
		mockMvc.perform(get("/api/teacher/toto"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testFindAll() throws Exception {
		mockMvc.perform(get("/api/teacher"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].lastName", is("DELAHAYE")))
		.andExpect(jsonPath("$[1].lastName", is("THIERCELIN")));
	}

}
