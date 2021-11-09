package br.com.alura.carteira.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.carteira.dto.UsuarioDetalhadoDto;
import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.modelo.Perfil;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.PerfilRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;

	public Page<UsuarioDto> listar(Pageable paginacao) {
		Page<Usuario> usuarios = usuarioRepository.findAll(paginacao);
		return usuarios.map(u -> modelMapper.map(u, UsuarioDto.class));
	}

	@Transactional
	public UsuarioDetalhadoDto cadastrar(UsuarioFormDto usuarioFormDto) {
		
		Boolean loginEmUso = usuarioRepository.existsByLogin(usuarioFormDto.getLogin());
				
		if (loginEmUso) {
			throw new IllegalArgumentException("O login informado já está em uso");
		}
		
		Usuario usuario = modelMapper.map(usuarioFormDto, Usuario.class);
		usuario.setId(null);
		
		usuarioFormDto.getPerfisDto().forEach(perfilDto -> {
			Perfil perfil = perfilRepository.getById(perfilDto.getId());
			usuario.adicionarPerfil(perfil);
		});
		
		String senha = new Random().nextInt(100000) + "";
		usuario.setSenha(bCryptPasswordEncoder.encode(senha));
		
		usuarioRepository.save(usuario);
		
		return modelMapper.map(usuario, UsuarioDetalhadoDto.class);
		
	}
	
	public UsuarioDetalhadoDto retornar(Long id) {
		
		Usuario usuario = usuarioRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		return modelMapper.map(usuario, UsuarioDetalhadoDto.class);
		
	}

	@Transactional
	public UsuarioDetalhadoDto atualizar(Long id, UsuarioFormDto usuarioFormDto) {
		
		Usuario usuario = usuarioRepository.getById(id);
		
		List<Perfil> perfis = usuarioFormDto.getPerfisDto().stream()
				.map(perfilDto -> perfilRepository.getById(perfilDto.getId()))
				.collect(Collectors.toList());
		
		usuario.atualizar(
				usuarioFormDto.getNome(), 
				usuarioFormDto.getLogin(),
				perfis
				);
		
		usuarioRepository.save(usuario);
		
		return modelMapper.map(usuario, UsuarioDetalhadoDto.class);
		
	}

	@Transactional
	public void remover(Long id) {	
		Usuario usuario = usuarioRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		if (!usuario.getTransacoes().isEmpty()) {
			throw new IllegalArgumentException("Um usuário só pode ser excluído se não tiver transações cadastradas");
		}
		usuarioRepository.deleteById(id);
	}

}
