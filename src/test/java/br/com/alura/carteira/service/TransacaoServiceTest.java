package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.dto.UsuarioDto;
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
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private TransacaoService transacaoService;
	
	private Usuario logado;
	
	@BeforeEach
	public void inicializaUsuarioLogado() {
		this.logado = new Usuario(null, "Antonio", "antonio", "123456");
	}
	
	private UsuarioDto criarUsuarioDto(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto(
				usuario.getId(), 
				usuario.getNome(), 
				usuario.getLogin());
		return usuarioDto;
	}
	
	private TransacaoFormDto criarTransacaoFormDto() {
		TransacaoFormDto transacaoFormDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("45.20"),
				150,
				LocalDate.now(),
				TipoTransacao.COMPRA,
				this.logado.getId()
				);
		return transacaoFormDto;
	}
	
	private Transacao criarTransacao(TransacaoFormDto transacaoFormDto) {
		Transacao transacao = new Transacao(
				transacaoFormDto.getTicker(),
				transacaoFormDto.getPreco(),
				transacaoFormDto.getQuantidade(),
				transacaoFormDto.getData(),
				transacaoFormDto.getTipo(),
				this.logado
				);
		return transacao;
	}
	
	private TransacaoDto criarTransacaoDto(Transacao transacao) {
		TransacaoDto transacaoDto = new TransacaoDto(
				null,
				transacao.getTicker(),
				transacao.getPreco(),
				transacao.getQuantidade(),
				transacao.getTipo()
				);
		return transacaoDto;
	}
	
	private TransacaoDetalhadaDto criarTransacaoDetalhadaDto(Transacao transacao) {
		TransacaoDetalhadaDto transacaoDetalhadaDto = new TransacaoDetalhadaDto(
				null,
				transacao.getTicker(),
				transacao.getPreco(),
				transacao.getQuantidade(),
				transacao.getTipo(),
				transacao.getData(),
				criarUsuarioDto(this.logado)
				);
		return transacaoDetalhadaDto;
	}
	
	private TransacaoFormDto criarTransacaoFormDtoParaAtualizacao() {
		TransacaoFormDto transacaoFormDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("45.20"),
				300,
				LocalDate.now(),
				TipoTransacao.VENDA,
				this.logado.getId()
				);
		return transacaoFormDto;
	}

	@Test
	void transacaoDeveSerCadastrada() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDto();
		
		Transacao transacao = criarTransacao(transacaoFormDto);
		
		TransacaoDetalhadaDto transacaoDetalhadaDto = criarTransacaoDetalhadaDto(transacao);
		
		Mockito
		.when(usuarioRepository.getById(transacaoFormDto.getUsuarioId()))
		.thenReturn(logado);
		
		Mockito
		.when(modelMapper.map(transacaoFormDto, Transacao.class))
		.thenReturn(transacao);
		
		Mockito
		.when(modelMapper.map(transacao, TransacaoDetalhadaDto.class))
		.thenReturn(transacaoDetalhadaDto);
		
		transacaoDetalhadaDto = transacaoService.cadastrar(transacaoFormDto, logado);
		
		Mockito.verify(transacaoRepository).save(Mockito.any());
		
		assertEquals(transacaoFormDto.getTicker(), transacaoDetalhadaDto.getTicker());
		assertEquals(transacaoFormDto.getPreco(), transacaoDetalhadaDto.getPreco());
		assertEquals(transacaoFormDto.getQuantidade(), transacaoDetalhadaDto.getQuantidade());
		assertEquals(transacaoFormDto.getData(), transacaoDetalhadaDto.getData());
		assertEquals(transacaoFormDto.getTipo(), transacaoDetalhadaDto.getTipo());
		assertEquals(transacaoFormDto.getUsuarioId(), transacaoDetalhadaDto.getUsuario().getId());
		
	}
	
	@Test
	void transacaoNaoDeveSerCadastradaPoisUsuarioNaoExiste() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDto();
		
		Mockito
		.when(usuarioRepository.getById(transacaoFormDto.getUsuarioId()))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(IllegalArgumentException.class, () -> transacaoService.cadastrar(transacaoFormDto, logado));
		
	}
	
	@Test
	void transacaoDeveSerAtualizada() {
		
		TransacaoFormDto transacaoFormDto = criarTransacaoFormDtoParaAtualizacao();
		
		Transacao transacao = criarTransacao(criarTransacaoFormDto());
		
		TransacaoDto transacaoDto = criarTransacaoDto(criarTransacao(transacaoFormDto));
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.getById(idTransacao))
		.thenReturn(transacao);
		
		Mockito
		.when(modelMapper.map(transacao, TransacaoDto.class))
		.thenReturn(transacaoDto);
		
		transacaoDto = transacaoService.atualizar(idTransacao, transacaoFormDto, logado);
		
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
		
		assertThrows(EntityNotFoundException.class, () -> transacaoService.atualizar(idTransacao, transacaoFormDto, logado));
		
	}
	
	@Test
	void transacaoDeveSerRemovida() {
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.findById(idTransacao))
		.thenReturn(Optional.of(criarTransacao(criarTransacaoFormDto())));
		
		transacaoService.remover(idTransacao, logado);
		
		Mockito.verify(transacaoRepository).delete(Mockito.any());
		
	}
	
	@Test
	void transacaoNaoDeveSerRemovidaPoisElaNaoExiste() {
		
		Long idTransacao = 1L;
		
		Mockito
		.when(transacaoRepository.findById(idTransacao))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> transacaoService.remover(idTransacao, logado));
		
	}

}
