package br.com.alura.carteira.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFormDto {

	@NotBlank
	private String nome;
	
	@NotBlank
	private String login;
	
	@NotBlank
	@Email
	private String email;

	@NotEmpty
	private List<PerfilDto> perfisDto;
	
}
