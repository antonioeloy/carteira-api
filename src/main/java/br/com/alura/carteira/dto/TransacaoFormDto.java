package br.com.alura.carteira.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.alura.carteira.modelo.TipoTransacao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoFormDto {

	@NotBlank
	@Size(min = 5, max = 6)
	private String ticker;
	
	@NotNull
	private BigDecimal preco;
	
	@NotNull
	@Min(1)
	private Integer quantidade;
	
	@PastOrPresent
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	@NotNull
	private TipoTransacao tipo;
	
	@NotNull
	private Long usuarioId;

}
