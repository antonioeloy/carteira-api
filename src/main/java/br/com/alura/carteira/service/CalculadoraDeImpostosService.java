package br.com.alura.carteira.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;

@Service
public class CalculadoraDeImpostosService {
	
	// 15% de imposto para transações do tipo VENDA com valor igual ou superior a R$20.000,00
	public BigDecimal calcular (Transacao transacao) {
		
		if (transacao.getTipo().equals(TipoTransacao.COMPRA)) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal valor = transacao
							.getPreco()
							.multiply(new BigDecimal(transacao.getQuantidade()));
		
		if (valor.compareTo(new BigDecimal("20000")) < 0) {
			return BigDecimal.ZERO;
		}
		
		return valor
				.multiply(new BigDecimal("0.15"))
				.setScale(2, RoundingMode.HALF_UP);
		
	}

}
