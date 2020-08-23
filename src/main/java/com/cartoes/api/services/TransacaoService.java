package com.cartoes.api.services;

import java.util.Optional;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@Service
public class TransacaoService {

	
	private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);
	 
	@Autowired
	private TransacaoRepository transacaoRepository;
	

 
   	public Optional<List<Transacao>> buscarPorcartaoNum(String num) throws ConsistenciaException {
 
         	log.info("Service: buscando transações vinculadas ao cartão de num: {}", num);
         	Optional<List<Transacao>> transacao = transacaoRepository.findByCartaoNum(num);
         	
         	if (!transacao.isPresent()) {
                	log.info("Service: Nenhum cartão com numero: {} foi encontrado", num);
                	throw new ConsistenciaException("Nenhum cartão com numero: {} foi encontrado", num);
         	}
         	return transacao;
   	}
   	
   	
   	
   	public void dataComparar(Date data) throws ConsistenciaException {
   		
   		log.info("Service: Verificando validade");
   		
   		Date data1 =new Date();
   		if(data1.after(data)) {	
   			throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o mesmo encontra-se vencido");
   		}
   	}
   	
   	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
 
         	log.info("Service: salvando o cartao: {}", transacao);
 
         	if (!transacaoRepository.findByCartaoNum(transacao.getCartao().getNumero()).isPresent()) {
 
                	log.info("Service: Nenhum cartão com numero: {} foi encontrado", transacao.getCartao().getNumero());
                	throw new ConsistenciaException("Nenhum cartão com numero: {} foi encontrado", transacao.getCartao().getNumero());
 
         	}
         	
         	if(transacao.getCartao().getBloqueado() == true ) {
         		
         		throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o mesmo encontra-se bloqueado.");
         	}
 
         	if (transacao.getCartao().getId() > 0) {
         		
         		throw new ConsistenciaException("Transações não podem ser alteradas, apenas incluídas.");
         		
         	}
         		
         	dataComparar(transacao.getCartao().getDataValidade());
 
         	try {
 
                	return transacaoRepository.save(transacao);
 
         	} catch (DataIntegrityViolationException e) {
 
                	log.info("Service: esta transação já foi cadastrada.");
                	throw new ConsistenciaException("Service: esta transação já foi cadastrada.");
                	
         	}
   	}
   	}