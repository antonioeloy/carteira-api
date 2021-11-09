package br.com.alura.carteira.service;

import java.math.BigDecimal;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CalculadoraDeImpostosService calculadoraDeImpostosService;

	public Page<TransacaoDto> listar(Pageable paginacao, Usuario logado) {
		Page<Transacao> transacoes = transacaoRepository.findAllByUsuario(paginacao, logado);
		return transacoes.map(t -> modelMapper.map(t, TransacaoDto.class));
	}

	@Transactional
	public TransacaoDetalhadaDto cadastrar(TransacaoFormDto transacaoFormDto, Usuario logado) {
		
		try {
			
			Usuario usuario = usuarioRepository.getById(transacaoFormDto.getUsuarioId());
			
			if (!usuario.equals(logado)) {
				this.lancaExcecaoAcessoNegado();
			}
			
			Transacao transacao = modelMapper.map(transacaoFormDto, Transacao.class);
			transacao.setId(null);
			transacao.setUsuario(usuario);
			
			BigDecimal imposto = calculadoraDeImpostosService.calcular(transacao);
			transacao.setImposto(imposto);
			
			transacaoRepository.save(transacao);
			
			return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
			
		} catch (EntityNotFoundException ex) {
			throw new IllegalArgumentException("Usuário inexistente");
		}
	}
	
	public TransacaoDetalhadaDto detalhar(Long id, Usuario logado) {
		
		Transacao transacao = transacaoRepository.
				findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		if (!transacao.cadastradaPeloUsuario(logado)) {
			this.lancaExcecaoAcessoNegado();
		}
		
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
		
	}

	@Transactional
	public TransacaoDetalhadaDto atualizar(Long id, TransacaoFormDto transacaoFormDto, Usuario logado) {
		
		Transacao transacao = transacaoRepository.getById(id);
		
		if (!transacao.cadastradaPeloUsuario(logado)) {
			this.lancaExcecaoAcessoNegado();
		}
		
		transacao.atualizar(
				transacaoFormDto.getTicker(),
				transacaoFormDto.getPreco(),
				transacaoFormDto.getQuantidade(),
				transacaoFormDto.getData(),
				transacaoFormDto.getTipo()
				);
		transacaoRepository.save(transacao);
		
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
		
	}

	@Transactional
	public void remover(Long id, Usuario logado) {	
		
		Transacao transacao = transacaoRepository.
				findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		if (!transacao.cadastradaPeloUsuario(logado)) {
			this.lancaExcecaoAcessoNegado();
		}
		
		transacaoRepository.delete(transacao);
		
	}
	
	private void lancaExcecaoAcessoNegado() {
		throw new AccessDeniedException("Acesso negado");
	}

}
