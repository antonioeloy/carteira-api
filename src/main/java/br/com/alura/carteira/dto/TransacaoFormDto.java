package br.com.alura.carteira.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.alura.carteira.modelo.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoFormDto {

	@NotBlank
	@Size(min = 5, max = 6)
	@Pattern(regexp = "[a-zA-z]{4}[0-9][0-9]?", message = "{transacao.ticker.invalido}")
	private String ticker;
	
	@NotNull
	@DecimalMin(value = "0.01")
	private BigDecimal preco;
	
	@NotNull
	@Min(value = 1)
	private Integer quantidade;
	
	@NotNull
	@PastOrPresent
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	@NotNull
	private TipoTransacao tipo;
	
	@NotNull
	@JsonAlias("usuario_id")
	private Long usuarioId;

}
