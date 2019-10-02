package br.com.krlsedu.onibusPoa.controller;

import br.com.krlsedu.onibusPoa.model.*;
import br.com.krlsedu.onibusPoa.service.IntinerarioService;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class LinhaController {
	
	private final LinhaService linhaService;
	private final IntinerarioService intinerarioService;
	
	@Autowired
	ReactiveMongoOperations operations;
	
	@Autowired
	public LinhaController(LinhaService linhaService, IntinerarioService intinerarioService) {
		this.linhaService = linhaService;
		this.intinerarioService = intinerarioService;
	}
	
	@ApiOperation(value = "Endpoint de cadastro de Linhas", response = Linha.class, tags = "CRUD das linhas")
	@PostMapping("/linhas")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Linha> create(
			@Valid @RequestBody final Linha linha) {
		return linhaService.salva(linha)
				.doOnNext(l -> log.debug("Nova linha criada - {}", l));
	}
	
	@ApiOperation(value = "Endpoint para integrar os dados de linhas da api do Poa transportes", response = Linha.class, tags = "Integração")
	@GetMapping("/linhas-integracao")
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<Linha> create() throws IOException, InterruptedException {
		WebClient linhasClient = WebClient.create("http://www.poatransporte.com.br/php/facades/process.php?a=nc&p=%&t=o");
		Mono<String> linhas = linhasClient.get()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<Linha> linhaList = objectMapper.readValue(linhas.block(), new TypeReference<List<Linha>>() {
		});
		return operations.insertAll(linhaList);
	}
	
	@ApiOperation(value = "Endpoint para consultar as linhas de ônibus passando as coordenadas atuias e o raio desejado", response = Linha.class, tags = "CRUD das linhas")
	@RequestMapping(value = "/linhas-por-localizacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Linha> getLinhasLoc(
			@RequestBody Localizacao locationRequest) {
		Point point = new Point(locationRequest.getLatitude(), locationRequest.getLongitude());
		Distance distance = new Distance(0.1, Metrics.KILOMETERS);
		return intinerarioService.buscaPorLocalizacao(point, distance).flatMap(intinerario -> linhaService.buscaPorCodigo(intinerario.getLinha().getCodigo()));
	}
	
	@ApiOperation(value = "Endpoint para consultar as linhas de ônibus passando as coordenadas atuias e o raio desejado", response = Linha.class, tags = "CRUD das linhas")
	@GetMapping(path = "/linhas", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Linha> buscaTodos() {
		return linhaService.buscaTodos();
	}
	
	@ApiOperation(value = "Endpoint para consultar as linhas de ônibus por nome", response = Linha.class, tags = "CRUD das linhas")
	@GetMapping(path = "/linhas/{nome}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Linha> buscaPorNome(@ApiParam("Nome da linha a ser consultada") @PathVariable String nome) {
		return linhaService.buscaPorNome(nome);
	}
	
	@ApiOperation(value = "Endpoint para deletar as linhas de ônibus por coodigo", response = Linha.class, tags = "CRUD das linhas")
	@DeleteMapping(path = "/linhas/{codigo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Void> deletaPorCodigo(@ApiParam("Código da linha a ser deletada") @PathVariable String codigo) {
		return linhaService.deletePorCodigo(codigo);
	}
}
