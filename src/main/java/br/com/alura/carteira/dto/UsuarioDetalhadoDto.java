package br.com.alura.carteira.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDetalhadoDto extends UsuarioDto {
	
	public UsuarioDetalhadoDto(String nome, String login, List<PerfilDto> perfis) {
		super(nome, login);
		this.perfis = perfis;
	}

	private List<PerfilDto> perfis;

}
