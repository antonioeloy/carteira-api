package br.com.alura.carteira.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoDetalhadaDto extends TransacaoDto {
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	private UsuarioDto usuario;

}
