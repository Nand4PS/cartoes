package com.cartoes.api.dtos;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import com.fasterxml.jackson.annotation.JsonBackReference;



public class TransacaoDto {

	
   	private int id;
	
	@NotEmpty(message = "Data de transação não pode ser vazio.")
   	private Date dataTransacao;
	
	@NotEmpty(message = "CNPJ não pode ser vazio.")
   	@CNPJ( message = "CNPJ inválido.")
	private String cnpj;
	
	@NotEmpty(message = "Valor não pode ser vazio.")
	@Length(min = 1, max = 10, message = "Valor deve conter até 10 caracteres numéricos.")
	private double valor;
	
	@NotEmpty(message = "Qntd de Parcelas não pode ser vazio.")
	@Length(min = 1, max = 2, message = "Qndt de Parcelas deve conter até 2 caracteres numéricos.")
	private int qdtParcelas;
	
	@NotEmpty(message = "Juros não pode ser vazio.")
	@Length(min = 1, max = 4, message = "Juros deve conter até 4 caracteres numéricos.")
	private double juros;
	
	@NotEmpty(message = "Id do cartão não pode ser vazio.")
   	String  Idcartao;

	
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
	
	public String getCartao() {
     	return Idcartao;
	}
	
	public void setCartao(String Idcartao) {
     	this.Idcartao = Idcartao;
	}
	
   	@Override
   	public String toString() {          	
   		return "Transacao[" + "id=" + id + ","                        	
                   + "dataTransacao=" + dataTransacao + ","
               	+ "cnpj=" + cnpj + ","
               	+ "valor=" + valor + ","
               	+ "qdtParcelas=" + qdtParcelas + ","
               	+ "juros=" + juros + "]";
}

}