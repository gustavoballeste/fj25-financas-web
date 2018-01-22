package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.GerenteDao;
import br.com.caelum.financas.modelo.Conta;

import br.com.caelum.financas.modelo.GerenteConta;

@Named
@ViewScoped
public class ContasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ContaDao contaDao;

    @Inject
    private GerenteDao gerenteDao;
    
    private Integer gerenteId;
    
	private Conta conta = new Conta();
	private List<Conta> contas = new ArrayList<Conta>();

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public void grava() {

		//código anterior
		// if(gerenteId != null){
		// Gerente gerenteRelacionado = gerenteDao.busca(gerenteId);
		// this.conta.setGerente(gerenteRelacionado);
		// }
		if(gerenteId != null){
			GerenteConta gerenteRelacionado = gerenteDao.busca(gerenteId);
			gerenteRelacionado.setNumeroDaConta(this.conta.getNumero());
			System.out.println("gerenteRelacionado.getId(): " + gerenteRelacionado.getId());
			this.conta.setGerente(gerenteRelacionado);
		}
		
		if (this.getConta() == null) {
			contaDao.adiciona(conta);
		} else {
			contaDao.altera(conta);
		}
		this.contas = contaDao.lista();
		System.out.println("Gravando a conta");

		limpaFormularioDoJSF();
	}

	public List<Conta> getContas() {
		if(this.contas.isEmpty()) {
			this.contas = contaDao.lista(); 
		}

		return contas;
	}

	public void remove() {
		System.out.println("Removendo a conta");
		
		contaDao.remove(conta);
		this.contas = contaDao.lista();
		
		limpaFormularioDoJSF();
	}

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento em que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.conta = new Conta();
	}

	public Integer getGerenteId() {
		return gerenteId;
	}

	public void setGerenteId(Integer gerenteId) {
		this.gerenteId = gerenteId;
	}
		
}
