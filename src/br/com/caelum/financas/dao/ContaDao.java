package br.com.caelum.financas.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.financas.modelo.Conta;

@Stateless 
public class ContaDao {

	@Inject
	EntityManager manager;

	public void adiciona(Conta conta) {
		manager.joinTransaction();
		this.manager.persist(conta);
	}

	public void altera(Conta conta) {
		manager.joinTransaction();
		this.manager.merge(conta);
	}
	
	public Conta busca(Integer id) {
		manager.joinTransaction();
		return this.manager.find(Conta.class, id);
	}

	public List<Conta> lista() {
		manager.joinTransaction();
		return this.manager.createQuery("select c from Conta c", Conta.class)
				.getResultList();
	}

	public void remove(Conta conta) {
		manager.joinTransaction();
		Conta contaParaRemover = this.manager.find(Conta.class, conta.getId());
		this.manager.remove(contaParaRemover);
	}
	
	public int trocaNomeDoBancoEmLote(String antigoNomeBanco, String novoNomeBanco) {
		
		String jpql = "UPDATE Conta c SET c.banco = :novoNomeBanco "
				+ "WHERE c.banco = :antigoNomeBanco";
		Query query = manager.createQuery(jpql);
		query.setParameter("antigoNomeBanco", antigoNomeBanco);
		query.setParameter("novoNomeBanco", novoNomeBanco);
		return query.executeUpdate();

	}

}




