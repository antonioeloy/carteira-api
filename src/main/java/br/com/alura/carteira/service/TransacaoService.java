package br.com.alura.carteira.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.repository.TransacaoRepository;

@Service
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRepository;

	private ModelMapper modelMapper = new ModelMapper();

	public Page<TransacaoDto> listar(Pageable paginacao) {
		Page<Transacao> transacoes = transacaoRepository.findAll(paginacao);
		return transacoes.map(t -> modelMapper.map(t, TransacaoDto.class));
	}

	@Transactional
	public void cadastrar(TransacaoFormDto transacaoFormDto) {
		modelMapper.typeMap(TransacaoFormDto.class, Transacao.class).addMappings(mapper -> mapper.skip(Transacao::setId));
		Transacao transacao = modelMapper.map(transacaoFormDto, Transacao.class);
		transacaoRepository.save(transacao);
	}

}
