package br.com.krlsedu.onibusPoa.controller;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.service.IntinerarioService;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
}
