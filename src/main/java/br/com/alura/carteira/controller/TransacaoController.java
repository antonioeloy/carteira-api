package br.com.alura.carteira.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.service.TransacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/transacoes")
@Api(tags = "Transação")
public class TransacaoController {
	
	@Autowired
	TransacaoService transacaoService;
	
	@GetMapping
	@ApiOperation("Listar transações")
	public Page<TransacaoDto> listar (@PageableDefault(size = 5) Pageable paginacao, 
			@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		Page<TransacaoDto> transacoes = transacaoService.listar(paginacao, logado);
		return transacoes;
	}
	
	@PostMapping
	@ApiOperation("Cadastrar uma transação")
	public ResponseEntity<TransacaoDetalhadaDto> cadastrar(@RequestBody @Valid TransacaoFormDto transacaoFormDto, 
			UriComponentsBuilder uriBuilder,
			@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		TransacaoDetalhadaDto transacaoDetalhadaDto = transacaoService.cadastrar(transacaoFormDto, logado);
		URI uri = uriBuilder
				.path("/transacoes/{id}")
				.buildAndExpand(transacaoDetalhadaDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(transacaoDetalhadaDto);
	}
	
	@PutMapping("/{id}")
	@ApiOperation("Atualizar uma transação")
	public ResponseEntity<TransacaoDto> atualizar(@PathVariable @NotNull Long id, 
			@RequestBody @Valid TransacaoFormDto transacaoFormDto,
			@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		TransacaoDto transacaoDto = transacaoService.atualizar(id, transacaoFormDto, logado);
		return ResponseEntity.ok(transacaoDto);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation("Remover uma transação")
	public ResponseEntity<Object> remover(@PathVariable @NotNull Long id, 
			@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		transacaoService.remover(id, logado);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Detalhar uma transação")
	public ResponseEntity<TransacaoDetalhadaDto> detalhar(@PathVariable @NotNull Long id, 
			@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		TransacaoDetalhadaDto transacaoDetalhadaDto = transacaoService.detalhar(id, logado);
		return ResponseEntity.ok(transacaoDetalhadaDto);
	}
	
}
