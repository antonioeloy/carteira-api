package br.com.alura.carteira.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.service.TransacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transacoes")
@Api(tags = "Transação")
public class TransacaoController {
	
	@Autowired
	TransacaoService transacaoService;
	
	@GetMapping
	@ApiOperation("Listar transações")
	public Page<TransacaoDto> listar (@PageableDefault(size = 5) Pageable paginacao) {
		Page<TransacaoDto> transacoes = transacaoService.listar(paginacao);
		return transacoes;
	}
	
	@PostMapping
	@ApiOperation("Cadastrar uma transação")
	public ResponseEntity<TransacaoDetalhadaDto> cadastrar(@RequestBody @Valid TransacaoFormDto transacaoFormDto, 
			UriComponentsBuilder uriBuilder) {
		TransacaoDetalhadaDto transacaoDetalhadaDto = transacaoService.cadastrar(transacaoFormDto);
		URI uri = uriBuilder
				.path("/transacoes/{id}")
				.buildAndExpand(transacaoDetalhadaDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(transacaoDetalhadaDto);
	}
	
	@PutMapping("/{id}")
	@ApiOperation("Atualizar uma transação")
	public ResponseEntity<TransacaoDto> atualizar(@PathVariable Long id, @RequestBody @Valid TransacaoFormDto transacaoFormDto) {
		TransacaoDto transacaoDto = transacaoService.atualizar(id, transacaoFormDto);
		return ResponseEntity.ok(transacaoDto);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation("Remover uma transação")
	public ResponseEntity<Object> remover(@PathVariable Long id) {
		transacaoService.remover(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Detalhar uma transação")
	public ResponseEntity<TransacaoDetalhadaDto> detalhar(@PathVariable Long id) {
		TransacaoDetalhadaDto transacaoDetalhadaDto = transacaoService.detalhar(id);
		return ResponseEntity.ok(transacaoDetalhadaDto);
	}
	
}
