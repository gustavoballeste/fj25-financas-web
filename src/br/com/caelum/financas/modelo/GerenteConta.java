package br.com.caelum.financas.modelo;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Audited
@Entity
public class GerenteConta extends Gerente {

	private static final long serialVersionUID = 1L;

	private String numeroDaConta;

	public String getNumeroDaConta() {
		return numeroDaConta;
	}

	public void setNumeroDaConta(String numeroDaConta) {
		this.numeroDaConta = numeroDaConta;
	}
	
	
}
