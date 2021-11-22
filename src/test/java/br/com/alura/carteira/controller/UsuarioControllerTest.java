package br.com.alura.carteira.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.carteira.dto.PerfilDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.infra.security.TokenService;
import br.com.alura.carteira.modelo.Perfil;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.PerfilRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private TokenService tokenService;
	
	private String token;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@BeforeEach
	private void gerarToken() {
		Usuario logado = new Usuario("Antonio", "antonio", "123456");
		Perfil admin = perfilRepository.findById(1L).get();
		logado.adicionarPerfil(admin);
		usuarioRepository.save(logado);
		Authentication authentication = new UsernamePasswordAuthenticationToken(logado, logado.getLogin());
		this.token = tokenService.gerarToken(authentication);
		
	}
	
	@Test
	void naoDeveriaCadastrarUsuarioComDadosIncompletos() throws Exception {
		
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto();		
		String json = objectMapper.writeValueAsString(usuarioFormDto);
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.header("Authorization", token)
				)
		.andExpect(
				status().isBadRequest()
				);			
		
	}
	
	@Test
	void deveriaCadastrarUsuarioComDadosCompletos() throws Exception {
		
		PerfilDto perfilDto = new PerfilDto(2L, "ROLE_COMUM");
		List<PerfilDto> listaPerfisDto = Arrays.asList(perfilDto);
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto("Antonio Eloy", "antonio.eloy@email.com.br", "antonio.eloy@email.com.br", listaPerfisDto);
		String json = objectMapper.writeValueAsString(usuarioFormDto);
		
		String jsonRetorno = "{\"nome\": \"Antonio Eloy\", \"login\": \"antonio.eloy@email.com.br\"}";
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.header("Authorization", token)
				)
		.andExpect(
				status().isCreated()
				)
		.andExpect(
				header().exists("Location")
				)
		.andExpect(
				content().json(jsonRetorno)
				);
		
	}
	
	@Test
	void naoDeveriaCadastrarUsuarioComLoginJaEmUso() throws Exception {
		
		PerfilDto perfilDto = new PerfilDto(1L, "ROLE_ADMIN");
		List<PerfilDto> listaPerfisDto = Arrays.asList(perfilDto);
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto("Antonio", "antonio", "antonio.eloy@email.com.br", listaPerfisDto);
		String json = objectMapper.writeValueAsString(usuarioFormDto);
		
		mvc
		.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.header("Authorization", token)
				)
		.andExpect(
				status().isBadRequest()
				);
		
	}

}
