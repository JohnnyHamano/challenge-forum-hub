package com.alura.forumhub.repository;

import com.alura.forumhub.model.User;
import com.alura.forumhub.model.dto.User_Data;
import com.alura.forumhub.model.dto.User_Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DataJpaTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest
{
	@Autowired UserRepository repository;
	@Autowired TestEntityManager manager;
//	@Autowired MockMvc mvc;
	
	@Autowired
	private JacksonTester<User_Register> userRegisterDTO;
	private JacksonTester<User_Data> userDataDTO;
	
	User_Register user_toRegister = new User_Register("username", "name@email.com", "password");
	User user_registered = new User(user_toRegister);
	
	@Test @WithMockUser
	@DisplayName("Returns null user when username or e-mail doesn't exists in database")
	void findActiveUserByUsernameOrEmail_1() {
		// Given~Arrange
		String usernameOrEmail = "user_that_dont_exists";
		
		// When~Act
		User user = repository.findActiveUserByUsernameOrEmail(usernameOrEmail);
		
		// Then~Assert
		assertThat(user).isNull();
	}
	
//	@Test @WithMockUser
//	@DisplayName("Returns null user when username or e-mail doesn't exists in database")
//	void findActiveUserByUsernameOrEmail_2() throws Exception {
//		// Given~Arrange
//		var response = mvc.perform(
//			post("/consultas").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(user_toRegister))
//			post("/consultas").contentType(MediaType.APPLICATION_JSON).content(userRegisterDTO.write(user_toRegister).getJson())
//		).andReturn().getResponse();
//
//		// When~Act
//		User user = repository.findActiveUserByUsernameOrEmail("");
//
//		when(agendaDeConsultas.agendar(any())).thenReturn(User);
//		var jsonEsperado = userDataDTO.write(dadosDetalhamento).getJson();
//
//
//		// Then~Assert
//		assertThat(user).isEqualTo(user_registered);
//	}
}