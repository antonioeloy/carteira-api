package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@InjectMocks
	private UsuarioService usuarioService;

	@Test
	void usuarioDeveSerCadastrado() {
		
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto(
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy@email.com.br"
				);
		
		UsuarioDto usuarioDto = usuarioService.cadastrar(usuarioFormDto);
		
		assertEquals(usuarioFormDto.getNome(), usuarioDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDto.getLogin());
		
	}

}
