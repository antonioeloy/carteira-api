package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.alura.carteira.dto.PerfilDto;
import br.com.alura.carteira.dto.UsuarioDetalhadoDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.modelo.Perfil;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.PerfilRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private PerfilRepository perfilRepository;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private UsuarioService usuarioService;
	
	private UsuarioFormDto criarUsuarioFormDto() {
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto(
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy150@email.com.br",
				"antonio.eloy150@email.com.br",
				List.of(new PerfilDto(1L, "ROLE_ADMIN"))
				);
		return usuarioFormDto;
	}
	
	private UsuarioFormDto criarUsuarioFormDtoAtualizado() {
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto(
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy300@email.com.br",
				"antonio.eloy300@email.com.br",
				List.of(new PerfilDto(1L, "ROLE_ADMIN"))
				);
		return usuarioFormDto;
	}
	
	private Usuario criarUsuario() {
		Usuario usuario = new Usuario(
				"Antonio Eloy de Oliveira Araujo",
				"antonio.eloy150@email.com.br",
				"123456"
				);
		return usuario;
	}
	
	private UsuarioDetalhadoDto criarUsuarioDetalhadoDto() {
		UsuarioDetalhadoDto usuarioDetalhadoDto = new UsuarioDetalhadoDto(
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy150@email.com.br",
				List.of(new PerfilDto(1L, "ROLE_ADMIN")));
		return usuarioDetalhadoDto;
	}
	
	private UsuarioDetalhadoDto criarUsuarioDetalhadoDtoAtualizado() {
		UsuarioDetalhadoDto usuarioDetalhadoDto = new UsuarioDetalhadoDto( 
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy300@email.com.br",
				List.of(new PerfilDto(1L, "ROLE_ADMIN")));
		return usuarioDetalhadoDto;
	}
	
	private Perfil criarPerfil() {
		Perfil perfil = new Perfil(1L, "ROLE_ADMIN");
		return perfil;
	}
	
	@Test
	void usuarioDeveSerCadastrado() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();		
		Boolean usuarioExistente = false;	
		Usuario usuario = criarUsuario();
		Perfil perfil = criarPerfil();
		UsuarioDetalhadoDto usuarioDetalhadoDto = criarUsuarioDetalhadoDto();
		
		Mockito
		.when(usuarioRepository.existsByLogin(usuarioFormDto.getLogin()))
		.thenReturn(usuarioExistente);
		
		Mockito
		.when(modelMapper.map(usuarioFormDto, Usuario.class))
		.thenReturn(usuario);
		
		Mockito
		.when(perfilRepository.getById(Mockito.any()))
		.thenReturn(perfil);
		
		Mockito
		.when(modelMapper.map(usuario, UsuarioDetalhadoDto.class))
		.thenReturn(usuarioDetalhadoDto);
		
		usuarioDetalhadoDto = usuarioService.cadastrar(usuarioFormDto);
		
		Mockito.verify(usuarioRepository).save(Mockito.any());
		
		assertEquals(usuarioFormDto.getNome(), usuarioDetalhadoDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDetalhadoDto.getLogin());
		
	}
	
	@Test
	void usuarioNaoDeveSerCadastradoPoisLoginJaEstaEmUso() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();		
		Boolean usuarioExistente = true;
		
		Mockito
		.when(usuarioRepository.existsByLogin(usuarioFormDto.getLogin()))
		.thenReturn(usuarioExistente);
		
		assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrar(usuarioFormDto));
		
	}
	
	@Test
	void usuarioDeveSerAtualizado() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDtoAtualizado();	
		Usuario usuario = criarUsuario();
		UsuarioDetalhadoDto usuarioDetalhadoDto = criarUsuarioDetalhadoDtoAtualizado();
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.getById(idUsuario))
		.thenReturn(usuario);
		
		Mockito
		.when(modelMapper.map(usuario, UsuarioDetalhadoDto.class))
		.thenReturn(usuarioDetalhadoDto);
		
		usuarioDetalhadoDto = usuarioService.atualizar(idUsuario,usuarioFormDto);
		
		Mockito.verify(usuarioRepository).save(Mockito.any());
		
		assertEquals(usuarioFormDto.getNome(), usuarioDetalhadoDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDetalhadoDto.getLogin());
		
	}
	
	@Test
	void usuarioNaoDeveSerAtualizadoPoisEleNaoExiste() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDtoAtualizado();	
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.getById(idUsuario))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> usuarioService.atualizar(idUsuario, usuarioFormDto));
		
	}
	
	@Test
	void usuarioDeveSerRemovido() {
		
		Usuario usuario = criarUsuario();
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenReturn(Optional.of(usuario));
		
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
		
		Usuario usuario = criarUsuario();
		usuario.getTransacoes().add(new Transacao());
		Long idUsuario = 1L;	
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenReturn(Optional.of(usuario));
		
		assertThrows(IllegalArgumentException.class, () -> usuarioService.remover(idUsuario));
		
	}

}
