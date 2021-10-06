package br.com.alura.carteira.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.modelo.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	@Query("select new br.com.alura.carteira.dto.ItemCarteiraDto("
			+ "t1.ticker, "
			+ "sum(t1.quantidade), "
			+ "(sum(t1.quantidade) / (select sum(t2.quantidade) from Transacao t2))*1.0) "
			+ "from Transacao t1 "
			+ "group by t1.ticker")
	public List<ItemCarteiraDto> relatorioCarteiraDeInvestimentos();

}
