package br.com.alura.carteira.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.dto.LoginFormDto;
import br.com.alura.carteira.dto.TokenDto;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException(""));
	}

	public TokenDto autenticar(LoginFormDto loginFormDto) {
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				loginFormDto.getLogin(), 
				loginFormDto.getSenha());
		
		authentication = authenticationManager.authenticate(authentication);
		
		return new TokenDto(tokenService.gerarToken(authentication));
		
	}

}
