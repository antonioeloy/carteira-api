package br.com.alura.carteira.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.alura.carteira.modelo.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDetalhadaDto extends TransacaoDto {

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	private UsuarioDto usuario;
	
	public TransacaoDetalhadaDto(Long id, String ticker, BigDecimal preco, Integer quantidade, TipoTransacao tipo,
			LocalDate data, UsuarioDto usuario) {
		super(id, ticker, preco, quantidade, tipo);
		this.data = data;
		this.usuario = usuario;
	}

}
