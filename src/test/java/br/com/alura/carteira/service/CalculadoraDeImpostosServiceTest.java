package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;

class CalculadoraDeImpostosServiceTest {
	
	private CalculadoraDeImpostosService calculadora;
	
	private Transacao criarTransacao(BigDecimal preco, TipoTransacao tipo) {
		Transacao transacao = new Transacao(
				15L,
				"BBSE3",
				preco,
				100,
				LocalDate.now(),
				tipo,
				new BigDecimal("0"),
				new Usuario(1L, "Antonio", "antonio.eloy@email.com.br", "123456")
				);
		return transacao;
	}
	
	@BeforeEach
	public void inicializar() {
		this.calculadora = new CalculadoraDeImpostosService();
	}

	@Test
	void transacaoDoTipoCompraNaoDeveTerImposto() {
		
		Transacao transacao = criarTransacao(new BigDecimal("30.00"), TipoTransacao.COMPRA);
		
		BigDecimal imposto = calculadora.calcular(transacao);
		
		assertEquals(BigDecimal.ZERO, imposto);
		
	}
	
	@Test
	void transacaoDoTipoVendaComValorMenorDoQueVinteMilNaoDeveTerImposto() {
		
		Transacao transacao = criarTransacao(new BigDecimal("30.00"), TipoTransacao.VENDA);
		
		BigDecimal imposto = calculadora.calcular(transacao);
		
		assertEquals(BigDecimal.ZERO, imposto);
		
	}
	
	@Test
	void transacaoDoTipoVendaComValorIgualOuMaiorDoQueVinteMilDeveTerImposto() {
		
		Transacao transacao = criarTransacao(new BigDecimal("300.00"), TipoTransacao.VENDA);
		
		BigDecimal imposto = calculadora.calcular(transacao);
		
		assertEquals(new BigDecimal("4500.00"), imposto);
		
	}

}
