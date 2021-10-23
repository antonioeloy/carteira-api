package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@InjectMocks
	private UsuarioService usuarioService;
	
	private Usuario criarUsuario() {
		Usuario usuario = new Usuario(
				1L, 
				"Antonio", 
				"antonio.eloy150@email.com.br",
				"123456"
				);
		return usuario;
	}
	
	private Optional<Usuario> criarOptionalUsuario() {
		Usuario usuario =  criarUsuario();
		return Optional.of(usuario);
	}
	
	private UsuarioFormDto criarUsuarioFormDto() {
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto(
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy150@email.com.br"
				);
		return usuarioFormDto;
	}

	@Test
	void usuarioDeveSerCadastrado() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();
		
		UsuarioDto usuarioDto = usuarioService.cadastrar(usuarioFormDto);
		
		Mockito.verify(usuarioRepository).save(Mockito.any());
		
		assertEquals(usuarioFormDto.getNome(), usuarioDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDto.getLogin());
		
	}
	
	@Test
	void usuarioNaoDeveSerCadastradoPoisLoginJaEstaEmUso() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();
		
		Mockito
		.when(usuarioRepository.findByLogin(usuarioFormDto.getLogin()))
		.thenReturn(criarOptionalUsuario());
		
		assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrar(usuarioFormDto));
		
	}
	
	@Test
	void usuarioDeveSerRetornado() {
		
		Usuario usuario = criarUsuario();
		
		Mockito
		.when(usuarioRepository.findById(usuario.getId()))
		.thenReturn(Optional.of(usuario));
		
		UsuarioDto usuarioDto = usuarioService.retornar(usuario.getId());
		
		assertEquals(usuario.getId(), usuarioDto.getId());
		assertEquals(usuario.getNome(), usuarioDto.getNome());
		assertEquals(usuario.getLogin(), usuarioDto.getLogin());
		
	}
	
	@Test
	void usuarioNaoDeveSerRetornadoPoisEleNaoExiste() {
		
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> usuarioService.retornar(idUsuario));
		
	}
	
	@Test
	void usuarioDeveSerAtualizado() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();
		
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.getById(idUsuario))
		.thenReturn(criarUsuario());
		
		UsuarioDto usuarioDto = usuarioService.atualizar(idUsuario,usuarioFormDto);
		
		Mockito.verify(usuarioRepository).save(Mockito.any());
		
		assertEquals(usuarioFormDto.getNome(), usuarioDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDto.getLogin());
		
	}
	
	@Test
	void usuarioNaoDeveSerAtualizadoPoisEleNaoExiste() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();
		
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.getById(idUsuario))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> usuarioService.atualizar(idUsuario, usuarioFormDto));
		
	}
	
	@Test
	void usuarioDeveSerRemovido() {
		
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenReturn(criarOptionalUsuario());
		
		usuarioService.remover(idUsuario);
		
		Mockito.verify(usuarioRepository).deleteById(Mockito.any());
		
	}
	
	@Test
	void usuarioNaoDeveSerRemovidoPoisEleNaoExiste() {
		
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> usuarioService.remover(idUsuario));
		
	}
	
	@Test
	void usuarioNaoDeveSerRemovidoPoisElePossuiTransacoesCadastradas() {
		
		Long idUsuario = 1L;
		
		Usuario usuario = criarUsuario();
		usuario.getTransacoes().add(new Transacao());
		Optional<Usuario >usuarioOptional = Optional.of(usuario);
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenReturn(usuarioOptional);
		
		assertThrows(IllegalArgumentException.class, () -> usuarioService.remover(idUsuario));
		
	}

}
