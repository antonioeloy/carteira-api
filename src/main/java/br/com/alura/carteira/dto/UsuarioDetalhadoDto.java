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
	
	private List<PerfilDto> perfis;

}
