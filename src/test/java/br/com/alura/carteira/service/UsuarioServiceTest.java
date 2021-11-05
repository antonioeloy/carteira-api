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
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.alura.carteira.dto.UsuarioDto;
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
				1l
				);
		return usuarioFormDto;
	}
	
	private UsuarioFormDto criarUsuarioFormDtoAtualizado() {
		UsuarioFormDto usuarioFormDto = new UsuarioFormDto(
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy300@email.com.br",
				1l
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
	
	private UsuarioDto criarUsuarioDto() {
		UsuarioDto usuarioDto = new UsuarioDto(null, 
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy150@email.com.br");
		return usuarioDto;
	}
	
	private UsuarioDto criarUsuarioDtoAtualizado() {
		UsuarioDto usuarioDto = new UsuarioDto(null, 
				"Antonio Eloy de Oliveira Araujo", 
				"antonio.eloy300@email.com.br");
		return usuarioDto;
	}
	
	private Perfil criarPerfil() {
		Perfil perfil = new Perfil(null, "ROLE_ADMIN");
		return perfil;
	}
	
	private Optional<Usuario> criarOptionalUsuario() {
		Usuario usuario =  criarUsuario();
		return Optional.of(usuario);
	}
	
	
	@Test
	void usuarioDeveSerCadastrado() {
		
		UsuarioFormDto usuarioFormDto = criarUsuarioFormDto();
		
		Boolean usuarioExistente = false;
		
		Usuario usuario = criarUsuario();
		
		Perfil perfil = criarPerfil();
		
		String senhaCriptografada = "$2a$10$Mbg2.cJMsmWXR5R.1D2u5uGGH8/UIejEdsxeeb.bXxXN3wkyC0adC";
		
		UsuarioDto usuarioDto = criarUsuarioDto();
		
		Mockito
		.when(usuarioRepository.existsByLogin(usuarioFormDto.getLogin()))
		.thenReturn(usuarioExistente);
		
		Mockito
		.when(modelMapper.map(usuarioFormDto, Usuario.class))
		.thenReturn(usuario);
		
		Mockito
		.when(perfilRepository.getById(usuarioFormDto.getPerfilId()))
		.thenReturn(perfil);
		
		Mockito
		.when(bCryptPasswordEncoder.encode(Mockito.anyString()))
		.thenReturn(senhaCriptografada);
		
		Mockito
		.when(modelMapper.map(usuario, UsuarioDto.class))
		.thenReturn(usuarioDto);
		
		usuarioDto = usuarioService.cadastrar(usuarioFormDto);
		
		Mockito.verify(usuarioRepository).save(Mockito.any());
		
		assertEquals(usuarioFormDto.getNome(), usuarioDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDto.getLogin());
		
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
		
		UsuarioDto usuarioDto = criarUsuarioDtoAtualizado();
		
		Long idUsuario = 1L;
		
		Mockito
		.when(usuarioRepository.getById(idUsuario))
		.thenReturn(usuario);
		
		Mockito
		.when(modelMapper.map(usuario, UsuarioDto.class))
		.thenReturn(usuarioDto);
		
		usuarioDto = usuarioService.atualizar(idUsuario,usuarioFormDto);
		
		Mockito.verify(usuarioRepository).save(Mockito.any());
		
		assertEquals(usuarioFormDto.getNome(), usuarioDto.getNome());
		assertEquals(usuarioFormDto.getLogin(), usuarioDto.getLogin());
		
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
		Optional<Usuario> usuarioOptional = Optional.of(usuario);
		
		Mockito
		.when(usuarioRepository.findById(idUsuario))
		.thenReturn(usuarioOptional);
		
		assertThrows(IllegalArgumentException.class, () -> usuarioService.remover(idUsuario));
		
	}

}
