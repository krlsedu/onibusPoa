package br.com.krlsedu.onibusPoa.controller;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.model.Localizacao;
import br.com.krlsedu.onibusPoa.model.LocalizacaoIntegracao;
import br.com.krlsedu.onibusPoa.service.IntinerarioService;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
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
	
	final
	ReactiveMongoOperations operations;
	
	@Autowired
	public IntinerarioController(LinhaService linhaService, IntinerarioService intinerarioService, ReactiveMongoOperations operations) {
		this.linhaService = linhaService;
		this.intinerarioService = intinerarioService;
		this.operations = operations;
	}
	
	@PostMapping("/intinerarios")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Intinerario> create(
			@Valid @RequestBody final Intinerario intinerario) {
		return intinerarioService.salva(intinerario)
				.doOnNext(l -> log.debug("Novo intinerario criado - {}", l));
	}
	
	@GetMapping("/intinerarios-integracao")
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<Intinerario> create() throws IOException, InterruptedException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Linha> linhaList = linhaService.buscaTodos().collectList().block();
		List<Intinerario> intinerariList = new ArrayList<>();
		assert linhaList != null;
		for (Linha linha :
				linhaList) {
			
			log.info("Processando intinerarios da linha - {}", linha);
			WebClient intinerarioClient = WebClient.create("http://www.poatransporte.com.br/php/facades/process.php?a=il&p=" + linha.getId());
			Mono<String> intinerarios = intinerarioClient.get()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(String.class);
			JsonNode intinerariosNode = new ObjectMapper().readTree(intinerarios.block());
			Iterator<Map.Entry<String, JsonNode>> iter = intinerariosNode.fields();
			//Foi adicionado o sleep para evitar que a api do poa transporte derrubasse a conexão;
			TimeUnit.MILLISECONDS.sleep(100);
			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> entry = iter.next();
				if (entry.getValue().isObject()) {
					LocalizacaoIntegracao localizacaoIntegracao = objectMapper.readValue(entry.getValue().toString(), LocalizacaoIntegracao.class);
					Intinerario e = new Intinerario( entry.getKey(), new Linha(intinerariosNode.get("codigo").asText()+"-"+intinerariosNode.get("idlinha").asText(), intinerariosNode.get("codigo").asText(), intinerariosNode.get("nome").asText()), new Double[]{localizacaoIntegracao.getLat(), localizacaoIntegracao.getLng()});
					intinerariList.add(e);
					log.info("Novo intinerario criado - {}", e);
				}
			}
		}
		return intinerarioService.salvaTodos(intinerariList);
	}
	
	@GetMapping(path = "/intinerarios", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Intinerario> buscaTodos() {
		return intinerarioService.buscaTodos()
				.doOnComplete(() -> log.debug("Listando todos os intinerarios"));
	}
	
	@RequestMapping(value = "/intinerarios-por-localizacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Intinerario> getAmbulanceDetails(
			@RequestBody Localizacao locationRequest) {
		Point point = new Point(locationRequest.getLatitude(), locationRequest.getLongitude());
		Distance distance = new Distance(1, Metrics.KILOMETERS);
		return intinerarioService.buscaPorLocalizacao(point, distance);
	}
}
