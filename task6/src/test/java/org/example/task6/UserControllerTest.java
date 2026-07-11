package org.example.task6;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String userJson;

	@BeforeEach
	void setUp() {
		userJson = "{\"name\":\"John\",\"email\":\"john@test.com\",\"age\":25}";
	}

	@Test
	void createUser_ShouldReturnCreated() throws Exception {
		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("John"))
				.andExpect(jsonPath("$.email").value("john@test.com"))
				.andExpect(jsonPath("$.age").value(25));
	}

	@Test
	void getAllUsers_ShouldReturnOk() throws Exception {
		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk());
	}

	@Test
	void getUserById_ShouldReturnUser() throws Exception {
		String response = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		UserDTO user = objectMapper.readValue(response, UserDTO.class);

		mockMvc.perform(get("/api/users/" + user.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.name").value("John"));
	}

	@Test
	void updateUser_ShouldReturnUpdatedUser() throws Exception {
		String response = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		UserDTO user = objectMapper.readValue(response, UserDTO.class);

		String updatedJson = "{\"name\":\"Johnny\",\"email\":\"johnny@test.com\",\"age\":26}";

		mockMvc.perform(put("/api/users/" + user.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatedJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Johnny"))
				.andExpect(jsonPath("$.age").value(26));
	}

	@Test
	void deleteUser_ShouldReturnNoContent() throws Exception {
		String response = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		UserDTO user = objectMapper.readValue(response, UserDTO.class);

		mockMvc.perform(delete("/api/users/" + user.getId()))
				.andExpect(status().isNoContent());
	}
}