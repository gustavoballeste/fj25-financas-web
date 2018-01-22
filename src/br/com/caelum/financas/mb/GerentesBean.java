package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.GerenteDao;
import br.com.caelum.financas.modelo.Gerente;
import br.com.caelum.financas.modelo.GerenteConta;

@Named
@ViewScoped
public class GerentesBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private GerenteDao gerenteDao;
	
	private List<Gerente> gerentes;
	
	private Gerente gerente = new GerenteConta();

	private void limpaFormularioDoJSF() {
		this.gerente = new GerenteConta();
	}
	
	public List<Gerente> getGerentes() {
		if (this.gerentes == null) {
			gerentes = gerenteDao.lista();
		}
		return gerentes;
	}

	public void grava() {
		if (this.getGerente() == null) {
			gerenteDao.adiciona(gerente);
		} else {
			gerenteDao.altera(gerente);
		}
		this.gerentes = gerenteDao.lista();
		System.out.println("Gravando o gerente");

		limpaFormularioDoJSF();
	}

	public void remove() {
		System.out.println("Removendo a conta");
		
		gerenteDao.remove(gerente);
		this.gerentes = gerenteDao.lista();
		
		limpaFormularioDoJSF();
	}
		
	public Gerente getGerente() {
		return gerente;
	}

	public void setGerente(Gerente gerente) {
		this.gerente = gerente;
	}


}
