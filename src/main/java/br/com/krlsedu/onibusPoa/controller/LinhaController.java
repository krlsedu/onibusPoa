package br.com.krlsedu.onibusPoa.controller;

import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

@Slf4j
@RestController
public class LinhaController {
	
	private final LinhaService linhaService;
	
	@Autowired
	public LinhaController(LinhaService linhaService) {
		this.linhaService = linhaService;
	}
	
	@PostMapping("/linhas")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Linha> create(
			@Valid @RequestBody final Linha linha) {
		return linhaService.salva(linha)
				.doOnNext(l -> log.debug("Nova linha criada - {}", l));
	}
	
	@GetMapping(path = "/linhas", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Linha> buscaTodos() {
		return linhaService.buscaTodos()
				.doOnComplete(() -> log.debug("Listando todas as linhas"));
	}
	
	@GetMapping(path = "/linhas/{nome}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Linha> buscaPorNome(@PathVariable String nome) {
		return linhaService.buscaPorNome(nome)
				.doOnComplete(() -> log.debug("Listando as linhas com nome {}",nome));
	}
}
