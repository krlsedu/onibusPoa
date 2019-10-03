package br.com.krlsedu.onibusPoa.configs;

import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.service.LinhaService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class LinhaHandler {
	private final LinhaService linhaService;
	
	@Autowired
	LinhaHandler(LinhaService LinhaService) {
		this.linhaService = LinhaService;
	}
	
	Mono<ServerResponse> getByCodigo(ServerRequest r) {
		return defaultReadResponse(this.linhaService.buscaPorCodigo(codigo(r)));
	}
	
	Mono<ServerResponse> all(ServerRequest r) {
		return defaultReadResponse(this.linhaService.buscaTodos());
	}
	
	Mono<ServerResponse> create(ServerRequest request) {
		Flux<Linha> flux = request
				.bodyToFlux(Linha.class)
				.flatMap(this.linhaService::salva);
		return defaultWriteResponse(flux);
	}
	
	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Linha> Linhas) {
		return Mono
				.from(Linhas)
				.flatMap(p -> ServerResponse
						.created(URI.create("/Linha/" + p.getNome()))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.build()
				);
	}
	
	private static Mono<ServerResponse> defaultReadResponse(Publisher<Linha> Linhas) {
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Linhas, Linha.class);
	}
	
	private static String codigo(ServerRequest r) {
		return r.pathVariable("codigo");
	}
}