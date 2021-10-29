package br.com.alura.carteira.infra.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${jjwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		
		return Jwts
				.builder()
				.setSubject(usuarioLogado.getId().toString())
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
		
	}
	
	public Boolean isValido(String token) {
		
		try {
			Jwts
			.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
		
	}

	public Long extrairIdUsuario(String token) {
		Claims claims = Jwts
			.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody();
		return Long.parseLong(claims.getSubject());
	}

}
