package br.com.alura.carteira.service;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private ModelMapper modelMapper = new ModelMapper();

	public Page<TransacaoDto> listar(Pageable paginacao) {
		Page<Transacao> transacoes = transacaoRepository.findAll(paginacao);
		return transacoes.map(t -> modelMapper.map(t, TransacaoDto.class));
	}

	@Transactional
	public TransacaoDetalhadaDto cadastrar(TransacaoFormDto transacaoFormDto) {
		
		try {
			
			Usuario usuario = usuarioRepository.getById(transacaoFormDto.getUsuarioId());
			
			modelMapper.typeMap(TransacaoFormDto.class, Transacao.class).addMappings(mapper -> mapper.skip(Transacao::setId));
			Transacao transacao = modelMapper.map(transacaoFormDto, Transacao.class);
			transacao.setUsuario(usuario);
			
			transacaoRepository.save(transacao);
			
			return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
			
		} catch (EntityNotFoundException ex) {
			throw new IllegalArgumentException("Usuário inexistente");
		}
	}

	@Transactional
	public TransacaoDto atualizar(Long id, TransacaoFormDto transacaoFormDto) {
		
		Transacao transacao = transacaoRepository.getById(id);	
		transacao.atualizar(
				transacaoFormDto.getTicker(),
				transacaoFormDto.getPreco(),
				transacaoFormDto.getQuantidade(),
				transacaoFormDto.getData(),
				transacaoFormDto.getTipo()
				);
		transacaoRepository.save(transacao);
		
		return modelMapper.map(transacao, TransacaoDto.class);
		
	}

	@Transactional
	public void remover(Long id) {	
		Transacao transacao = transacaoRepository.
				findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		transacaoRepository.delete(transacao);
	}

	public TransacaoDetalhadaDto detalhar(Long id) {
		
		Transacao transacao = transacaoRepository.
				findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
		
	}

}
