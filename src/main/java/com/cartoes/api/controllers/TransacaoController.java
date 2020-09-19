package com.cartoes.api.controllers;
 
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.response.Response;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;
 
@RestController
@RequestMapping("/api/Transacao")
@CrossOrigin(origins = "*")
public class TransacaoController {
 
   	private static final Logger log = LoggerFactory.getLogger(CartaoController.class);
 
   	@Autowired
   	private TransacaoService transacaoService;
 

   	@GetMapping(value = "/cartao/{cartaoNum}")
   	public ResponseEntity<Response<List<TransacaoDto>>> buscarPorCartaoNum(@PathVariable("cartaoNum") String cartaoNum) {
 
   		Response<List<TransacaoDto>> response = new Response<List<TransacaoDto>>();
   		
         	try {
 
                	log.info("Controller: buscando transações do cartão de numero: {}", cartaoNum);
 
                	Optional<List<Transacao>> listaTra = transacaoService.buscarPorNumeroCartao(cartaoNum);
                		
                	response.setDados(ConversaoUtils.ConverterListaTr(listaTra.get()));
                	
                	return ResponseEntity.ok(response);
 
         	} catch (ConsistenciaException e) {
                	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
                	return ResponseEntity.badRequest().body(response);
         	} catch (Exception e) {
                	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
                	return ResponseEntity.status(500).body(response);
         	}
 
   	}
 

   	@PostMapping
   	public ResponseEntity<Response<TransacaoDto>> salvar(@Valid @RequestBody TransacaoDto transacaoDto, BindingResult result) {
		 
     	Response<TransacaoDto> response = new Response<TransacaoDto>();

     	try {

            	log.info("Controller: salvando o cartao: {}", transacaoDto.toString());

            	if (result.hasErrors()) {

                   	for (int i = 0; i < result.getErrorCount(); i++) {
                   	   	response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
                   	}

                   	log.info("Controller: Os campos obrigatórios não foram preenchidos");
                   	return ResponseEntity.badRequest().body(response);

            	}
     	
            	Transacao transacao = this.transacaoService.salvar(ConversaoUtils.ConverterTr(transacaoDto));
            	response.setDados(ConversaoUtils.ConverterTr(transacao));

            	return ResponseEntity.ok(response);

     	} catch (ConsistenciaException e) {

            	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
            	response.adicionarErro(e.getMensagem());
            	return ResponseEntity.badRequest().body(response);

     	} catch (Exception e) {

            	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
            	response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
            	return ResponseEntity.status(500).body(response);

     	}

	}
}