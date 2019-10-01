package br.com.krlsedu.onibusPoa.controller;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.model.Localizacao;
import br.com.krlsedu.onibusPoa.service.IntinerarioService;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

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
	public Mono<Intinerario> create(
			@Valid @RequestBody final Intinerario intinerario) {
		return intinerarioService.salva(intinerario)
				.doOnNext(l -> log.debug("Novo intinerario criado - {}", l));
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
//        List<Ambulance> ambulanceList=this.ambulanceRepo.findByLocationNear(new Point(Double.valueOf(locationRequest.getLongitude()),Double.valueOf(locationRequest.getLatitude())),new Distance(locationRequest.getDistance(), Metrics.KILOMETERS));
		Point point = new Point(locationRequest.getLatitude(), locationRequest.getLongitude());
		Distance distance = new Distance(1, Metrics.KILOMETERS);
		return intinerarioService.buscaPorLocalizacao(point, distance);
	}
}
