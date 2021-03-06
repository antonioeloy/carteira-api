package br.com.alura.carteira.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

import br.com.alura.carteira.dto.UsuarioDetalhadoDto;
import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
@Api(tags = "Usuário")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping
	@ApiOperation("Listar usuários")
	public Page<UsuarioDto> listar (@PageableDefault(size = 5) Pageable paginacao) {
		Page<UsuarioDto> usuarios = usuarioService.listar(paginacao);
		return usuarios;
	}
	
	@PostMapping
	@ApiOperation("Cadastrar um usuário")
	public ResponseEntity<UsuarioDetalhadoDto> cadastrar(@RequestBody @Valid UsuarioFormDto usuarioFormDto,
			UriComponentsBuilder uriBuilder) {
		UsuarioDetalhadoDto usuarioDetalhadoDto = usuarioService.cadastrar(usuarioFormDto);
		URI uri = uriBuilder
				.path("/usuarios/{id}")
				.buildAndExpand(usuarioDetalhadoDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(usuarioDetalhadoDto);
	}
	
	@PutMapping("/{id}")
	@ApiOperation("Atualizar um usuário")
	public ResponseEntity<UsuarioDetalhadoDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid UsuarioFormDto usuarioFormDto) {
		UsuarioDetalhadoDto usuarioDetalhadoDto = usuarioService.atualizar(id, usuarioFormDto);
		return ResponseEntity.ok(usuarioDetalhadoDto);
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Retornar um usuário")
	public ResponseEntity<UsuarioDetalhadoDto> retornar(@PathVariable @NotNull Long id) {
		UsuarioDetalhadoDto usuarioDetalhadoDto = usuarioService.retornar(id);
		return ResponseEntity.ok(usuarioDetalhadoDto);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation("Remover um usuário")
	public ResponseEntity<Object> remover(@PathVariable @NotNull Long id) {
		usuarioService.remover(id);
		return ResponseEntity.noContent().build();
	}

}
