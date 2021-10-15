package br.com.alura.carteira.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.modelo.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	@Query("select new br.com.alura.carteira.dto.ItemCarteiraDto("
			+ "t1.ticker, "
			+ "sum(CASE WHEN (t1.tipo = 'COMPRA') THEN t1.quantidade ELSE (t1.quantidade * -1) END), "
			+ "(select sum(CASE WHEN (t2.tipo = 'COMPRA') THEN t2.quantidade ELSE (t2.quantidade * -1) END) from Transacao t2)) "
			+ "from Transacao t1 "
			+ "group by t1.ticker")
	public List<ItemCarteiraDto> relatorioCarteiraDeInvestimentos();

}
