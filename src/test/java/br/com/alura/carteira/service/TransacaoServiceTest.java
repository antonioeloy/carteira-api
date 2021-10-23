package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
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
	
	private Transacao criarTransacao() {
		Transacao transacao = new Transacao(
				1L,
				"ITSA4",
				new BigDecimal("45.20"),
				150,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				new Usuario(1L, "Antonio", "antonio.eloy@email.com.br", "123456")
				);
		return transacao;
	}
	
	private Optional<Transacao> criarOptionalTransacao() {
		Transacao transacao = new Transacao(
				1L,
				"ITSA4",
				new BigDecimal("45.20"),
				150,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				new Usuario(1L, "Antonio", "antonio.eloy@email.com.br", "123456")
				);
		return Optional.of(transacao);
	}
	
	private TransacaoFormDto criarTransacaoFormDto() {
		TransacaoFormDto transacaoFormDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("45.20"),
				150,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				1L
				);
		return transacaoFormDto;
	}
	
	private TransacaoFormDto criarTransacaoFormDtoParaAtualizacao() {
		TransacaoFormDto transacaoFormDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("45.20"),
				300,
				LocalDate.now(),
				TipoTransacao.VENDA,
				1L
				);
		return transacaoFormDto;
	}

	@Test
	void transacaoDeveSerCadastrada() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDto();
		
		TransacaoDto transacaoDto = transacaoService.cadastrar(transacaoFormDto);
		
		Mockito.verify(transacaoRepository).save(Mockito.any());
		
		assertEquals(transacaoFormDto.getTicker(), transacaoDto.getTicker());
		assertEquals(transacaoFormDto.getPreco(), transacaoDto.getPreco());
		assertEquals(transacaoFormDto.getQuantidade(), transacaoDto.getQuantidade());
		assertEquals(transacaoFormDto.getTipo(), transacaoDto.getTipo());
		
	}
	
	@Test
	void transacaoNaoDeveSerCadastradaPoisUsuarioNaoExiste() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDto();
		
		Mockito
		.when(usuarioRepository.getById(transacaoFormDto.getUsuarioId()))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(IllegalArgumentException.class, () -> transacaoService.cadastrar(transacaoFormDto));
		
	}
	
	@Test
	void transacaoDeveSerDetalhada() {
		
		Transacao transacao = criarTransacao();
		
		Mockito
		.when(transacaoRepository.findById(transacao.getId()))
		.thenReturn(Optional.of(transacao));
		
		TransacaoDto transacaoDto = transacaoService.detalhar(transacao.getId());
		
		assertEquals(transacao.getTicker(), transacaoDto.getTicker());
		assertEquals(transacao.getPreco(), transacaoDto.getPreco());
		assertEquals(transacao.getQuantidade(), transacaoDto.getQuantidade());
		assertEquals(transacao.getTipo(), transacaoDto.getTipo());
		
	}
	
	@Test
	void transacaoNaoDeveSerDetalhadaPoisElaNaoExiste() {
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.findById(idTransacao))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> transacaoService.detalhar(idTransacao));
		
	}
	
	@Test
	void transacaoDeveSerAtualizada() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDtoParaAtualizacao();
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.getById(idTransacao))
		.thenReturn(criarTransacao());
		
		TransacaoDto transacaoDto = transacaoService.atualizar(idTransacao, transacaoFormDto);
		
		Mockito.verify(transacaoRepository).save(Mockito.any());
		
		assertEquals(transacaoFormDto.getTicker(), transacaoDto.getTicker());
		assertEquals(transacaoFormDto.getPreco(), transacaoDto.getPreco());
		assertEquals(transacaoFormDto.getQuantidade(), transacaoDto.getQuantidade());
		assertEquals(transacaoFormDto.getTipo(), transacaoDto.getTipo());
		
	}
	
	@Test
	void transacaoNaoDeveSerAtualizadaPoisElaNaoExiste() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDtoParaAtualizacao();
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.getById(idTransacao))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> transacaoService.atualizar(idTransacao, transacaoFormDto));
		
	}
	
	@Test
	void transacaoDeveSerRemovida() {
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.findById(idTransacao))
		.thenReturn(criarOptionalTransacao());
		
		transacaoService.remover(idTransacao);
		
		Mockito.verify(transacaoRepository).delete(Mockito.any());
		
	}
	
	@Test
	void transacaoNaoDeveSerRemovidaPoisElaNaoExiste() {
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.findById(idTransacao))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> transacaoService.remover(idTransacao));
		
	}

}
