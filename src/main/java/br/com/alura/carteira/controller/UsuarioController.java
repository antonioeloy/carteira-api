package br.com.alura.carteira.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
	public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioFormDto usuarioFormDto,
			UriComponentsBuilder uriBuilder) {
		UsuarioDto usuarioDto = usuarioService.cadastrar(usuarioFormDto);
		URI uri = uriBuilder
				.path("/usuarios/{id}")
				.buildAndExpand(usuarioDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(usuarioDto);
	}
	
	@PutMapping("/{id}")
	@ApiOperation("Atualizar um usuário")
	public ResponseEntity<UsuarioDto> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioFormDto usuarioFormDto) {
		UsuarioDto usuarioDto = usuarioService.atualizar(id, usuarioFormDto);
		return ResponseEntity.ok(usuarioDto);
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Retornar um usuário")
	public ResponseEntity<UsuarioDto> retornar(@PathVariable Long id) {
		UsuarioDto usuarioDto = usuarioService.retornar(id);
		return ResponseEntity.ok(usuarioDto);
	}

}
