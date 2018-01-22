package br.com.caelum.financas.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		manager.joinTransaction();
		this.manager.persist(movimentacao);
		if (movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("Movimentação");
		}
	}

	public void altera(Movimentacao movimentacao) {
		manager.joinTransaction();
		this.manager.merge(movimentacao);
	}

	public Movimentacao busca(Integer id) {
		manager.joinTransaction();
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		manager.joinTransaction();
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public List<Movimentacao> listaComCategorias() {
		manager.joinTransaction();
		return this.manager.createQuery("select distinct m from Movimentacao m left join fetch m.categorias", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		manager.joinTransaction();
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}
	
	public BigDecimal calculaTotalMovimentacaoDao(Conta conta, TipoMovimentacao tipo) {
		manager.joinTransaction();
		
		String jpql = "select sum(m.valor) from Movimentacao m where m.conta = :conta and m.tipoMovimentacao = :tipo";
		
		TypedQuery<BigDecimal> query = this.manager.createQuery(jpql, BigDecimal.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		return query.getSingleResult();
		
	}

	public List<ValorPorMesEAno>listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo) {
		manager.joinTransaction();
		
		String jpql = "select new br.com.caelum.financas.modelo.ValorPorMesEAno(month(m.data), year(m.data), sum(m.valor)) "
				+ "from Movimentacao m "
				+ "where m.conta = :conta and m.tipoMovimentacao = :tipo) "
				+ "group by year(m.data) month(m.data) "
				+ "order by sum(m.valor) desc";
		
		TypedQuery<ValorPorMesEAno> query = this.manager.createQuery(jpql, ValorPorMesEAno.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}
	
	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {
		manager.joinTransaction();
		String jpql = "select m from Movimentacao m "
				+ "where m.conta = :conta order by m.valor desc";
		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("conta", conta);
		return query.getResultList();
	}
	
	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo) {
		manager.joinTransaction();
		String jpql = "select m from Movimentacao m " + 
				"where m.valor <= :valor and m.tipoMovimentacao = :tipo";
		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		
		query.setHint("org.hibernate.cacheable", "true");

		return query.getResultList();
	}
	
	public List<Movimentacao> buscaPorDescricao(String descricao) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);
		Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer( Movimentacao.class );
		QueryParser parser = new QueryParser("descricao",analyzer);
		try {
			org.apache.lucene.search.Query query = parser.parse(descricao);
			FullTextQuery textQuery = fullTextEntityManager.createFullTextQuery(query, Movimentacao.class);
			return textQuery.getResultList();
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}	
}
