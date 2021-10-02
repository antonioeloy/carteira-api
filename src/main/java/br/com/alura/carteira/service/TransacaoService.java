package br.com.alura.carteira.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.Transacao;

@Service
public class TransacaoService {
	
	private List<Transacao> transacoes = new ArrayList<>();
	private ModelMapper modelMapper = new ModelMapper();
	
	public List<TransacaoDto> listar() {
		return this.transacoes
					.stream()
					.map(t -> modelMapper.map(t, TransacaoDto.class))
					.collect(Collectors.toList());
	}

	public void cadastrar(TransacaoFormDto transacaoFormDto) {
		Transacao transacao = modelMapper.map(transacaoFormDto, Transacao.class);
		this.transacoes.add(transacao);
	}

}
