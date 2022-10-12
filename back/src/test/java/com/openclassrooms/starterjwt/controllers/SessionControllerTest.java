package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SessionControllerTest {

	@Autowired
	public MockMvc mockMvc;
	
	@Test
	@WithMockUser
	public void testFindById() throws Exception {		
		MvcResult result = 
		mockMvc.perform(post("/api/session")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
        .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");
		
		mockMvc.perform(get("/api/session/" + id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("session 1")));
	}
	
	@Test
	@WithMockUser
	public void testNotFoundFindById() throws Exception {
		mockMvc.perform(get("/api/session/100"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void testNotAuthFindById() throws Exception {
		mockMvc.perform(get("/api/session/1"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser
	public void testNumberFormatFindById() throws Exception {
		mockMvc.perform(get("/api/session/toto"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testFindAll() throws Exception {
		mockMvc.perform(post("/api/session")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		mockMvc.perform(get("/api/session"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].name", is("session 1")));
	}
	
	@Test
	@WithMockUser
	public void testCreate() throws Exception {		
		mockMvc.perform(post("/api/session")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
        .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("session 1")));
	}
	
	@Test
	@WithMockUser
	public void testUpdate() throws Exception {	
		MvcResult result = mockMvc.perform(post("/api/session")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");
		
		mockMvc.perform(put("/api/session/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"session 1 updated\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
        .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("session 1 updated")));
	}
	
	@Test
	@WithMockUser
	public void testNumberFormatUpdate() throws Exception {
		mockMvc.perform(put("/api/session/toto")
				.contentType(MediaType.APPLICATION_JSON)
		        .content("{\"name\": \"session 1 updated\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testSave() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/session")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("session 1")))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");
		
		mockMvc.perform(delete("/api/session/" + id))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testSaveNotFound() throws Exception {	
		mockMvc.perform(delete("/api/session/100"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void testNotAuthSave() throws Exception {
		mockMvc.perform(delete("/api/session/1"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser
	public void testNumberFormatSave() throws Exception {
		mockMvc.perform(delete("/api/session/toto"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testParticipate() throws Exception {
		mockMvc.perform(post("/api/session/1/participate/2"))
		.andExpect(status().isOk());
		
		mockMvc.perform(post("/api/session/1/participate/2"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testParticipateNotFound() throws Exception {
		mockMvc.perform(post("/api/session/1/participate/100"))
		.andExpect(status().isNotFound());
		
		mockMvc.perform(post("/api/session/100/participate/1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser
	public void testParticipateNumberFormat() throws Exception {
		mockMvc.perform(post("/api/session/toto/participate/toto"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testNoLongerParticipate() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/session")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("session 1")))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");
		
		
		mockMvc.perform(post("/api/session/"+id+"/participate/1"))
		.andExpect(status().isOk());
		
		mockMvc.perform(delete("/api/session/"+id+"/participate/1"))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testNoLongerParticipateNotAlready() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/session")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content("{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}") 
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("session 1")))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");
	
		mockMvc.perform(delete("/api/session/"+id+"/participate/1"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void testNoLongerParticipateNotFound() throws Exception {
		mockMvc.perform(delete("/api/session/100/participate/1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser
	public void testNoLongerParticipateNumberFormat() throws Exception {
		mockMvc.perform(delete("/api/session/toto/participate/toto"))
		.andExpect(status().isBadRequest());
	}

	
}
