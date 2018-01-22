package br.com.caelum.financas.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Gerente;
import br.com.caelum.financas.modelo.GerenteConta;

@Stateless
public class GerenteDao {

	@Inject
	private EntityManager manager;
	
	public void adiciona(Gerente gerente) {
		manager.joinTransaction();
		this.manager.persist(gerente);
	}

	public void altera(Gerente gerente) {
		manager.joinTransaction();
		this.manager.merge(gerente);
	}
	
	public GerenteConta busca(Integer id) {
		manager.joinTransaction();
		return this.manager.find(GerenteConta.class, id);
	}

	public List<Gerente> lista() {
		manager.joinTransaction();
		return this.manager.createQuery("select g from Gerente g", Gerente.class)
				.getResultList();
	}

	public void remove(Gerente gerente) {
		manager.joinTransaction();
		this.manager.remove(gerente);
	}
}
