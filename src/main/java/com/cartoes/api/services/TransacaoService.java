package com.cartoes.api.services;

 
import java.util.List; 
import java.util.Optional;
import java.util.Date;
 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.dao.DataIntegrityViolationException; 
import org.springframework.stereotype.Service;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository; 
import com.cartoes.api.repositories.TransacaoRepository; 
import com.cartoes.api.utils.ConsistenciaException;
 
@Service
public class TransacaoService {
    	private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);
 
	   	@Autowired
	   	private TransacaoRepository transacaoRepository;
 
	   	@Autowired
	   	private CartaoRepository cartaoRepository;
 
	   	public Optional<List<Transacao>> buscarPorNumeroCartao (String numeroCartao) throws ConsistenciaException {
 
         	log.info("Service: buscando as transações do cartão de número: {}", numeroCartao);  
	         	
         	Optional<List<Transacao>> transacoes = transacaoRepository.findByNumeroCartao(numeroCartao);
 
	         	if (!transacoes.isPresent() || transacoes.get().size() < 1) {
 
                log.info("Service: Nenhuma transação encontrada para o cartão de número: {}", numeroCartao);                 
                throw new ConsistenciaException("Service: Nenhuma transação encontrada para o cartão de número: {}", numeroCartao);
 
	         	}
 
	         	return transacoes;
 
	   	}
 
	   	
 
	   	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
 
	         	log.info("Service: salvando a transacao: {}", transacao);
	         	
	         	Optional<Cartao> cartao = cartaoRepository.findById(transacao.getCartao().getId());
 
	         	
	         	if (!cartao.isPresent()) {
 
                log.info("Service: Nenhum cartão de número: {} foi encontrado", transacao.getCartao().getId());
                	throw new ConsistenciaException("Nenhum cartão de número: {} foi encontrado", transacao.getCartao().getNumero());
 
	         	}
 
         	if (transacao.getId() > 0) {
         		log.info("Transações não podem ser alteradas, apenas incluídas");
         		throw new ConsistenciaException("Transações não podem ser alteradas, apenas incluídas");
         	}
         	if(cartao.get().getBloqueado()){
                log.info("Service: Não é possível encontrar transacões para esse cartão, pois o mesmo encontrá-se bloqueado");
               throw new ConsistenciaException("Service: Não é possível encontrar transacões para esse cartão, pois o mesmo encontrá-se bloqueado");
   
 
}
         	if(cartao.get().getDataValidade().before(new Date())) {
         		 log.info("Service: Não é possível encontrar transacões para esse cartão, pois o mesmo encontrá-se vencido");
         		 throw new ConsistenciaException("Service: Não é possível encontrar transacões para esse cartão, pois o mesmo encontrá-se vencido");
         	}
         	transacao.setCartao(cartao.get());
         	
         	return transacaoRepository.save(transacao);
     }
}