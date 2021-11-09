package br.com.alura.carteira.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
	
	private Long id;
	private String nome;
	private String login;
	
	public UsuarioDto(String nome, String login) {
		this.nome = nome;
		this.login = login;
	}

}
