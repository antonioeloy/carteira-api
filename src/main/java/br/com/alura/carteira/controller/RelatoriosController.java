package br.com.alura.carteira.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.service.RelatorioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/relatorios")
@Api(tags = "Relatórios")
public class RelatoriosController {
	
	@Autowired
	private RelatorioService relatorioService;

	@GetMapping("/carteira")
	@ApiOperation("Lista de itens da carteira de investimentos")
	public List<ItemCarteiraDto> relatorioCarteiraDeInvestimentos() {
		return relatorioService.relatorioCarteiraDeInvestimentos();
	}

}
