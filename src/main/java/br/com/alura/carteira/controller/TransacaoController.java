package br.com.alura.carteira.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.service.TransacaoService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	@Autowired
	TransacaoService transacaoService;
	
	@GetMapping
	public Page<TransacaoDto> listar (@PageableDefault(size = 5) Pageable paginacao) {
		Page<TransacaoDto> transacoes = transacaoService.listar(paginacao);
		return transacoes;
	}
	
	@PostMapping
	public void cadastrar(@RequestBody @Valid TransacaoFormDto transacaoFormDto) {
		transacaoService.cadastrar(transacaoFormDto);
	}
	
}
