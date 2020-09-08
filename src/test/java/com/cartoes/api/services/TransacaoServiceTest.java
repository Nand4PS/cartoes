package com.cartoes.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Transacao;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@MockBean
	
        public class TransacaoServiceTest {

        private TransacaoRepository transacaoRepository;
        private CartaoRepository cartaoRepository;
	
	@Autowired
	    private TransacaoService transacaoService;
        private CartaoService cartaoService;
	
	@Test
	    public void testBuscarPorIdExistente() throws ConsistenciaException {	
		
		BDDMockito.given(cartaoRepository.findById(Mockito.anyInt()))
			.willReturn(Optional.of(new Cartao()));
		
		Optional<Cartao> resultado = cartaoService.buscarPorId(1);
		
		assertTrue(resultado.isPresent());
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {	
		
		BDDMockito.given(cartaoRepository.findById(Mockito.anyInt()))
			.willReturn(Optional.empty());
		
		cartaoService.buscarPorId(1);
		
	}
	
	@Test
	public void testBuscarPorNumeroCartao() throws ConsistenciaException {	
		
		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
			.willReturn(Optional.of(new ArrayList<Transacao>()));
		
		Optional<List<Transacao>> resultado = transacaoService.buscarPorNumeroCartao("2541369951357423");
		
		assertTrue(resultado.isPresent());
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorNumeroCartaoNaoExistente() throws ConsistenciaException {	
		
		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
		.willReturn(null);
		
		transacaoService.buscarPorNumeroCartao("2541369951357423");
		
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {	
		
		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
			.willReturn(new Transacao());
		
	     Transacao resultado = transacaoService.salvar(new Transacao());
		
		assertNotNull(resultado);
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarIdNaoEncontrado() throws ConsistenciaException {	
		
		BDDMockito.given(cartaoRepository.findById(Mockito.anyInt()))
		.willReturn(Optional.empty());
		
		Cartao c = new Cartao();
		c.setId(1);
		
		cartaoService.salvar(c);

	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarNumeroCartaoDuplicado() throws ConsistenciaException {	
		
		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
		.willThrow(new DataIntegrityViolationException(""));
		
		transacaoService.salvar(new Transacao());

	}
	
}