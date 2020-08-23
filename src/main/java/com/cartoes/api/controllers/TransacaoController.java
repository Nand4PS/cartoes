package com.cartoes.api.controllers;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
 
@RestController
@RequestMapping("/api/Transacao")
@CrossOrigin(origins = "*")
public class TransacaoController {
 
   	private static final Logger log = LoggerFactory.getLogger(CartaoController.class);
 
   	@Autowired
   	private TransacaoService transacao;
 

   	@GetMapping(value = "/cartao/{cartaonum}")
   	public ResponseEntity<List<Transacao>> buscarPorCartaoNum(@PathVariable("cartaonum") String num) {
 
         	try {
         		
                	log.info("Controller: buscando transações do cartão de numero: {}", num);
 
                	Optional<List<Transacao>> listaTransacoes = transacao.buscarPorcartaoNum(num);
 
                	return ResponseEntity.ok(listaTransacoes.get());
 
         	} catch (ConsistenciaException e) {
                	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
                	return ResponseEntity.badRequest().body(new ArrayList<Transacao>());
         	} catch (Exception e) {
                	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
                	return ResponseEntity.status(500).body(new ArrayList<Transacao>());
         	}
 
   	}
 

   	@PostMapping
   	public ResponseEntity<Transacao> salvar(@RequestBody Transacao transacao) {
 
         	try {
 
                	log.info("Controller: salvando Transação: {}", transacao.toString());
         	
                	return ResponseEntity.ok(this.transacao.salvar(transacao));
 
         	} catch (ConsistenciaException e) {
                	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
                	return ResponseEntity.badRequest().body(new Transacao());
         	} catch (Exception e) {
                	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
                	return ResponseEntity.status(500).body(new Transacao());
         	}
 
   	}
 
}
