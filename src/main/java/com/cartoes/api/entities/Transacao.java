package com.cartoes.api.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table (name = "transacao")
public class Transacao {

	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO)
   	private int id;
	
	@Column(name = "data_Transacao", nullable = false)
   	private Date dataTransacao;
	
	@Column(name = "cnpj", nullable = false, length = 14, unique = true)
	private String cnpj;
	
	@Column(name= "valor", nullable = false)
	private double valor;
	
	@Column(name = "qndt_Parcelas", nullable = false)
	private int qdtParcelas;
	
	@Column(name= "juros", nullable = false)
	private double juros;
	
	@JsonBackReference
   	@ManyToOne(fetch = FetchType.EAGER)
   	private Cartao cartao;

	
	public int getId() {
     	return id;
	}
	
	public void setId(int id) {
     	this.id = id;
	}
	
	public Date getDataTransacao() {
     	return dataTransacao;
	}
	
	public void setDataTransacao(Date dataTransacao) {
     	this.dataTransacao = dataTransacao; 
	}
	
	public String getCnpj() {
     	return cnpj;
	}
	
	public void setcnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public double getvalor() {
     	return valor;
	}
	
	public void setvalor(double valor) {
     	this.valor = valor;
	}
	
	public int getqdtParcelas() {
     	return qdtParcelas;
	}
	
	public void setqdtParcelas(int qdtParcelas) {
     	this.qdtParcelas = qdtParcelas;
	}

	public double getjuros() {
     	return juros;
	}
	
	public void setjuros(double juros) {
     	this.juros = juros;
	}
	
	public Cartao getCartao() {
     	return cartao;
	}
	
	public void setCartao(Cartao cartao) {
     	this.cartao = cartao;
	}
	
	@PreUpdate
   	public void preUpdate() {
         	dataTransacao = new Date();
   	}
 
   	@PrePersist
   	public void prePersist() {
         	dataTransacao = new Date();
   	}
 
   	@Override
   	public String toString() {          	
   		return "Transacao[" + "id=" + id + ","                        	
   	                    + "dataTransacao=" + dataTransacao + ","
                       	+ "cnpj=" + cnpj + ","
                       	+ "valor=" + valor + ","
                       	+ "qdtParcelas=" + qdtParcelas + ","
                       	+ "Juros=" + juros + "]";
   	}



}