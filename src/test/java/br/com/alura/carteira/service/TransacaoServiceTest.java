package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {
	
	@Mock
	private TransacaoRepository transacaoRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@InjectMocks
	private TransacaoService transacaoService;

	@Test
	void transacaoDeveSerCadastrada() {
		
		TransacaoFormDto transacaoFormDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("45.20"),
				150,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				1L
				);
		
		TransacaoDto transacaoDto = transacaoService.cadastrar(transacaoFormDto);
		
		assertEquals(transacaoFormDto.getTicker(), transacaoDto.getTicker());
		assertEquals(transacaoFormDto.getPreco(), transacaoDto.getPreco());
		assertEquals(transacaoFormDto.getQuantidade(), transacaoDto.getQuantidade());
		assertEquals(transacaoFormDto.getTipo(), transacaoDto.getTipo());
		
	}
	
	@Test
	void transacaoNaoDeveSerCadastradaPoisUsuarioNaoExiste() {
		
		TransacaoFormDto transacaoFormDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("45.20"),
				150,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				100L
				);
		
		Mockito
		.when(usuarioRepository.getById(transacaoFormDto.getUsuarioId()))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(IllegalArgumentException.class, () -> transacaoService.cadastrar(transacaoFormDto));
		
	}

}
