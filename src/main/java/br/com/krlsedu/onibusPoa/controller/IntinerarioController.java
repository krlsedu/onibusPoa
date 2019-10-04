package br.com.krlsedu.onibusPoa.controller;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.model.LocalizacaoIntegracao;
import br.com.krlsedu.onibusPoa.service.IntinerarioService;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class IntinerarioController {
	
	private final LinhaService linhaService;
	private final IntinerarioService intinerarioService;
	
	@Autowired
	public IntinerarioController(LinhaService linhaService, IntinerarioService intinerarioService) {
		this.linhaService = linhaService;
		this.intinerarioService = intinerarioService;
	}
	
	@PostMapping("/intinerarios")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Endpoint para criar e alterar intinerários", response = Intinerario.class, tags = "CRUD dos intinerários")
	public Mono<Intinerario> create(
			@Valid @RequestBody final Intinerario intinerario) {
		return intinerarioService.salva(intinerario);
	}
	
	@GetMapping("/intinerarios-integracao")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Endpoint para integrar os dados de intinerarios da api do Poa transportes", response = Linha.class, tags = "Integração", notes = "devido ao grande volume de dados processados, pode levara um tempo significativo para responder")
	public Flux<Intinerario> create() throws InterruptedException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Linha> linhaList = linhaService.buscaTodos().collectList().block();
		List<Intinerario> intinerariList = new ArrayList<>();
		assert linhaList != null;
		Integer i = 0;
		for (Linha linha :
				linhaList) {
			i++;
			log.info("Processando intinerarios da linha {} de {} - {}", i, linhaList.size(), linha);
			WebClient intinerarioClient = WebClient.create("http://www.poatransporte.com.br/php/facades/process.php?a=il&p=" + linha.getId());
			Mono<String> intinerarios = intinerarioClient.get()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(String.class);
			try {
				JsonNode intinerariosNode = new ObjectMapper().readTree(intinerarios.block());
				Iterator<Map.Entry<String, JsonNode>> iter = intinerariosNode.fields();
				//Foi adicionado o sleep para evitar que a api do poa transporte derrubasse a conexão;
				TimeUnit.MILLISECONDS.sleep(100);
				while (iter.hasNext()) {
					Map.Entry<String, JsonNode> entry = iter.next();
					if (entry.getValue().isObject()) {
						LocalizacaoIntegracao localizacaoIntegracao = objectMapper.readValue(entry.getValue().toString(), LocalizacaoIntegracao.class);
						Intinerario e = new Intinerario(entry.getKey(), new Linha(intinerariosNode.get("codigo").asText() + "-" + intinerariosNode.get("idlinha").asText(), intinerariosNode.get("codigo").asText(), intinerariosNode.get("nome").asText()), new Double[]{localizacaoIntegracao.getLat(), localizacaoIntegracao.getLng()});
						intinerariList.add(e);
					}
				}
			} catch (IOException e) {
				log.error("Algo errado aconteceu - {}", e.getMessage());
			}
		}
		return intinerarioService.salvaTodos(intinerariList);
	}
	
	@GetMapping(path = "/intinerarios", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ApiOperation(value = "Endpoint para consultar os intinerarios cadastrados", response = Intinerario.class, tags = "CRUD dos intinerários")
	@ResponseStatus(HttpStatus.OK)
	public Flux<Intinerario> buscaTodos() {
		return intinerarioService.buscaTodos();
	}
	
	@ApiOperation(value = "Endpoint para deletar as intinerario por ponto", response = Intinerario.class, tags = "CRUD dos intinerários")
	@DeleteMapping(path = "/intinerarios/{ponto}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Void> deletaPorCodigo(@ApiParam("Ponto da linha a ser deletada") @PathVariable String ponto) {
		return intinerarioService.deletePorPonto(ponto);
	}
}
