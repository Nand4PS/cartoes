package com.api.cartoes.test;


import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import com.cartoes.api.entities.Cliente;
import com.cartoes.api.repositories.ClienteRepository;
import com.cartoes.api.services.ClienteService;
import com.cartoes.api.utils.ConsistenciaException;
 
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestIntegracao {
 
   	@Autowired
   	private ClienteRepository clienteRepository;
   	
   	private ClienteService se;
   	
   	private Cliente clienteTeste;
   	
   	
   	@Before
   	private void CriarClienteRepository() throws ParseException, ConsistenciaException {
         	
         	clienteTeste = new Cliente();
         	clienteTeste.setNome("Nome Teste");
         	clienteTeste.setCpf("05887098082");
         	clienteTeste.setUf("CE");
         	clienteTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
         	
         		clienteRepository.save(clienteTeste);

   	}
   	
   	@Test
   	private void CriarClienteServiceParaRepository() throws ParseException, ConsistenciaException {
         	
         	clienteTeste = new Cliente();
         	clienteTeste.setNome("Nome Teste");
         	clienteTeste.setCpf("05887098082");
         	clienteTeste.setUf("CE");
         	clienteTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
    
         		Cliente resultado = se.salvar(clienteTeste);
         		assertNotNull(resultado);
   	}
   	
   	
   	@Test
   	private void BuscarClienteCpfServiceParaRepository() throws ParseException, ConsistenciaException{

   		Optional<Cliente> resultado = se.buscarPorCpf(clienteTeste.getCpf());
   		
   		assertNotNull(resultado);

   	}
   	
   	@Test
   	private void BuscarClienteIDServiceParaRepository() throws ParseException, ConsistenciaException{

   		Optional<Cliente> resultado = se.buscarPorId(clienteTeste.getId());
   		
   		assertNotNull(resultado);

   	}
   	
   	
   	@After
   	public void tearDown() throws Exception {
         	
         	clienteRepository.deleteAll();
         	
   	}
   	
   	
 
}