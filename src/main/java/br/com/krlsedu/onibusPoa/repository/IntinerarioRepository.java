package br.com.krlsedu.onibusPoa.repository;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IntinerarioRepository extends ReactiveCrudRepository<Intinerario, Long> {
	Flux<Intinerario> findByLinha(Mono<Linha> linha);
	Flux<Intinerario> findByLocationNear(Point p, Distance d);
	
	Mono<Intinerario> findByPonto(Mono<String> ponto);
}
