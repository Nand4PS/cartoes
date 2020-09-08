package com.cartoes.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.cartoes.api.CartoesApplication;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.ClienteRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@RunWith
(SpringRunner.class)
@SpringBootTest
(classes= CartoesApplication.class)
@ActiveProfiles
("test")
public class TransacaoRepositoryTest {

	@Autowired
	private ClienteRepository cliRepository;  
	
	private Cliente clienteTeste;
	
	private CartaoRepository carRepository;
	
	private Cartao cartaoTeste;
	
	private TransacaoRepository tranRepository;
	
	private Transacao tranTeste;
	
	private void CriarTrTestes() throws ParseException {
		
		clienteTeste = new Cliente();
		
		clienteTeste.setNome("Teste Nome");
		clienteTeste.setCpf("05743162012");
		clienteTeste.setUf("PR");
		clienteTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("30/01/2020"));
		
		cartaoTeste.setBloqueado(false);
		cartaoTeste.setCliente(clienteTeste);
		cartaoTeste.setDataAtualizacao(new SimpleDateFormat("dd/mm/yyy").parse("01/02/2020"));
		cartaoTeste.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2028"));
		cartaoTeste.setNumero("2541369951357423");
		
		
		tranTeste.setCartao(cartaoTeste);
		tranTeste.setcnpj("13469758543279");
		tranTeste.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		tranTeste.setjuros(6);
		tranTeste.setqdtParcelas(3);
		tranTeste.setvalor(150.30);
		
	}
	
	@Before
	public void setUp() throws Exception {
		
		CriarTrTestes();
	
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		
		cliRepository.deleteAll();
		carRepository.deleteAll();
		tranRepository.deleteAll();
	}
	
	
	@Test
	public void testAliment() {
		

		cliRepository.save(clienteTeste);
		carRepository.save(cartaoTeste);
		tranRepository.save(tranTeste);
		
		Cliente cli = cliRepository.findByCpf(clienteTeste.getCpf());
		Optional<Cartao> car = carRepository.findById(cartaoTeste.getId());
		Optional<Transacao> tran = tranRepository.findById(1);
		
		assertEquals(tranTeste, tran);
		assertEquals(cartaoTeste, car);
		assertEquals(clienteTeste, cli);
	}
	
	@Test
	public void testFindByCartao() {	
		
		
		Transacao Tran = tranRepository.findById(tranTeste.getId()).get();
		assertEquals(tranTeste.getId(), Tran.getId());
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void TestChangTr() {
	
		tranTeste.setId(1);
		
		tranRepository.save(tranTeste);
		
	}
	

}