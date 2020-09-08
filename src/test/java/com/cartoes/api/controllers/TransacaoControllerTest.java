package com.cartoes.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransacaoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TransacaoService transacao;
	

	private Transacao CriarTrTeste() throws ParseException  {

		Transacao tran = new Transacao();
		Cartao car = new Cartao();
		
		tran.setId(1);
        tran.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
        tran.setcnpj("13469758543279");
        tran.setqdtParcelas(3);
        tran.setvalor(150.30);
        tran.setjuros(6);
        tran.setCartao(car);
		
        return tran;

	}

	@Test
	@WithMockUser
	public void testBuscarPorIdSucesso() throws Exception {

		Transacao tran = CriarTrTeste();

		BDDMockito.given(transacao.buscarPorNumeroCartao(Mockito.anyString()))
		.willReturn(Optional.of(new ArrayList<Transacao>()));

		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.dados.id").value(tran.getId()))
			.andExpect(jsonPath("$.dados.cnpj").value(tran.getCnpj()))
			.andExpect(jsonPath("$.dados.data_Transacao").value(tran.getDataTransacao()))
			.andExpect(jsonPath("$.dados.qdt_Parcelas").value(tran.getqdtParcelas()))
			.andExpect(jsonPath("$.dados.juros").value(tran.getjuros()))
			.andExpect(jsonPath("$.dados.valor").value(tran.getvalor()))
			.andExpect(jsonPath("$.dados.cartao_id").value(tran.getCartao().getId()))
			.andExpect(jsonPath("$.erros").isEmpty());

	}

	@Test
	@WithMockUser
	public void testBuscarPorIdInconsistencia() throws Exception {

		BDDMockito.given(transacao.buscarPorNumeroCartao((Mockito.anyString())))
			.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	

	@Test
	@WithMockUser
	public void testSalvarSucesso() throws Exception {

		Transacao tran = CriarTrTeste();
		TransacaoDto objEntrada = ConversaoUtils.ConverterTr(tran);

		String json = new ObjectMapper().writeValueAsString(objEntrada);
		
		BDDMockito.given(transacao.salvar(Mockito.any(Transacao.class)))
			.willReturn(tran);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.dados.id").value(tran.getId()))
			.andExpect(jsonPath("$.dados.cnpj").value(tran.getCnpj()))
			.andExpect(jsonPath("$.dados.data_Transacao").value(tran.getDataTransacao()))
			.andExpect(jsonPath("$.dados.qdt_Parcelas").value(tran.getqdtParcelas()))
			.andExpect(jsonPath("$.dados.juros").value(tran.getjuros()))
			.andExpect(jsonPath("$.dados.valor").value(tran.getvalor()))
			.andExpect(jsonPath("$.dados.cartao_id").value(tran.getCartao().getId()))
			.andExpect(jsonPath("$.erros").isEmpty());

	}
	
	@Test
	@WithMockUser
	public void testSalvarInconsistencia() throws Exception {

		Transacao tran = CriarTrTeste();
		
		TransacaoDto objEntrada = ConversaoUtils.ConverterTr(tran);

		String json = new ObjectMapper().writeValueAsString(objEntrada);
		
		BDDMockito.given(transacao.salvar(Mockito.any(Transacao.class)))
			.willThrow(new ConsistenciaException("Teste inconsistência."));
		
		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Teste inconsistência."));

	}
	
	@Test
	@WithMockUser
	public void testSalvarValorEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setcnpj("13469758543279");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(6);
		objEntrada.setqdtParcelas(3);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/cliente")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Valor não pode ser vazio."));

	}
	
	
	@Test
	@WithMockUser
	public void testSalvarValorExcedente() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setcnpj("13469758543279");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(6);
		objEntrada.setqdtParcelas(3);
		objEntrada.setvalor(154456890.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Valor deve conter até 10 caracteres numéricos."));

	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosExcedente() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setcnpj("13469758543279");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(61456);
		objEntrada.setqdtParcelas(3);
		objEntrada.setvalor(150.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Juros deve conter até 4 caracteres numéricos."));

	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setcnpj("13469758543279");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setqdtParcelas(3);
		objEntrada.setvalor(150.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Juros não pode ser vazio."));

	}
	
	@Test
	@WithMockUser
	public void testSalvarParcelasEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setcnpj("13469758543279");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(6);
		objEntrada.setvalor(150.30);


		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Qntd de Parcelas não pode ser vazio."));

	}
	
	
	@Test
	@WithMockUser
	public void testSalvarParcelaExcedente() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setcnpj("13469758543279");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(5);
		objEntrada.setqdtParcelas(354);
		objEntrada.setvalor(150.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Qndt de Parcelas deve conter até 2 caracteres numéricos."));

	}
	
	
	@Test
	@WithMockUser
	public void testSalvarDataEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setcnpj("13469758543279");
		objEntrada.setjuros(6);
		objEntrada.setqdtParcelas(3);
		objEntrada.setvalor(150.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Data de transação não pode ser vazio."));

	}
	

	@Test
	@WithMockUser
	public void testSalvarCnpjEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(6);
		objEntrada.setqdtParcelas(3);
		objEntrada.setvalor(150.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Cnpj não pode ser vazio."));

	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjInvalido() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setcnpj("1111111111111");
		objEntrada.setDataTransacao(new SimpleDateFormat("dd/mm/yyy").parse("01/03/2020"));
		objEntrada.setjuros(6);
		objEntrada.setqdtParcelas(3);
		objEntrada.setvalor(150.30);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("CNPJ inválido."));

	}
	
}