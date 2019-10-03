import br.com.krlsedu.onibusPoa.service.LinhaService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class LinhaHandler {
	
	// <1>
	private final LinhaService linhaService;
	
	LinhaHandler(LinhaService LinhaService) {
		this.linhaService = LinhaService;
	}
	
	// <2>
	Mono<ServerResponse> getById(ServerRequest r) {
		return defaultReadResponse(this.linhaService.get(id(r)));
	}
	
	Mono<ServerResponse> all(ServerRequest r) {
		return defaultReadResponse(this.linhaService.all());
	}
	
	Mono<ServerResponse> deleteById(ServerRequest r) {
		return defaultReadResponse(this.linhaService.delete(id(r)));
	}
	
	Mono<ServerResponse> updateById(ServerRequest r) {
		Flux<Linha> id = r.bodyToFlux(Linha.class)
				.flatMap(p -> this.linhaService.update(id(r), p.getEmail()));
		return defaultReadResponse(id);
	}
	
	Mono<ServerResponse> create(ServerRequest request) {
		Flux<Linha> flux = request
				.bodyToFlux(Linha.class)
				.flatMap(toWrite -> this.linhaService.create(toWrite.getEmail()));
		return defaultWriteResponse(flux);
	}
	
	// <3>
	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Linha> Linhas) {
		return Mono
				.from(Linhas)
				.flatMap(p -> ServerResponse
						.created(URI.create("/Linhas/" + p.getId()))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.build()
				);
	}
	
	// <4>
	private static Mono<ServerResponse> defaultReadResponse(Publisher<Linha> Linhas) {
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Linhas, Linha.class);
	}
	
	private static String id(ServerRequest r) {
		return r.pathVariable("id");
	}
}