package br.com.alura.carteira.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void deveriaCadastrarUsuarioComDadosCompletos() throws Exception {
		
		String json = "{\"nome\": \"Antonio Eloy\", \"login\": \"antonio.eloy@email.com.br\"}";
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				)
		.andExpect(
				status().isCreated()
				)
		.andExpect(
				header().exists("Location")
				)
		.andExpect(
				content().json(json)
				);
		
	}

	@Test
	void naoDeveriaCadastrarUsuarioComDadosIncompletos() throws Exception {
		
		String json = "{}";
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				)
		.andExpect(
				status().isBadRequest()
				);			
		
	}
	
	@Test
	void naoDeveriaCadastrarUsuarioComLoginJaEmUso() throws Exception {
		
		String json1 = "{\"nome\": \"Antonio Eloy\", \"login\": \"antonio.eloy@email.com.br\"}";
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json1)
				)
		.andExpect(
				status().isCreated()
				)
		.andExpect(
				header().exists("Location")
				)
		.andExpect(
				content().json(json1)
				);
		
		String json2 = "{\"nome\": \"Antonio Eloy de Oliveira Araujo\", \"login\": \"antonio.eloy@email.com.br\"}";
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json2)
				)
		.andExpect(
				status().isBadRequest()
				);
		
	}
	
	@Test
	void deveriaRetornarUmUsuario() throws Exception {
		
		String json = "{\"nome\": \"Antonio Eloy\", \"login\": \"antonio.eloy@email.com.br\"}";
		
		MvcResult resultadoPostUsuario = mvc
				.perform(
						post("/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						)
				.andExpect(
						status().isCreated()
						)
				.andExpect(
						header().exists("Location")
						)
				.andExpect(
						content().json(json)
						)
				.andReturn();
		
		String location = resultadoPostUsuario.getResponse().getHeader("Location");
		
		String idUsuario = location.substring(location.lastIndexOf("/") + 1);
		
		mvc
		.perform(
				get("/usuarios/" + idUsuario)
				)
		.andExpect(
				status().isOk()
				)
		.andExpect(
				content().json(json)
				);
				
	}

}
