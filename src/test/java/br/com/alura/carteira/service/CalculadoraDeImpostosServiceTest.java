package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;

class CalculadoraDeImpostosServiceTest {

	@Test
	void transacaoDoTipoCompraNaoDeveTerImposto() {
		
		Transacao transacao = new Transacao(
				15L,
				"BBSE3",
				new BigDecimal("30.00"),
				100,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				new Usuario(1L, "Antonio", "antonio.eloy@email.com.br", "123456")
				);
		
		BigDecimal imposto = new CalculadoraDeImpostosService().calcular(transacao);
		
		assertEquals(BigDecimal.ZERO, imposto);
		
	}
	
	@Test
	void transacaoDoTipoVendaComValorMenorDoQueVinteMilNaoDeveTerImposto() {
		
		Transacao transacao = new Transacao(
				15L,
				"BBSE3",
				new BigDecimal("30.00"),
				100,
				LocalDate.now(),
				TipoTransacao.VENDA,
				new Usuario(1L, "Antonio", "antonio.eloy@email.com.br", "123456")
				);
		
		BigDecimal imposto = new CalculadoraDeImpostosService().calcular(transacao);
		
		assertEquals(BigDecimal.ZERO, imposto);
		
	}
	
	@Test
	void transacaoDoTipoVendaComValorIgualOuMaiorDoQueVinteMilDeveTerImposto() {
		
		Transacao transacao = new Transacao(
				15L,
				"BBSE3",
				new BigDecimal("300.00"),
				100,
				LocalDate.now(),
				TipoTransacao.VENDA,
				new Usuario(1L, "Antonio", "antonio.eloy@email.com.br", "123456")
				);
		
		BigDecimal imposto = new CalculadoraDeImpostosService().calcular(transacao);
		
		assertEquals(new BigDecimal("4500.00"), imposto);
		
	}

}
