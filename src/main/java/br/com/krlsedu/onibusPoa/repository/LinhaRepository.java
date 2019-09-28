package br.com.krlsedu.onibusPoa.repository;

import br.com.krlsedu.onibusPoa.model.Linha;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LinhaRepository extends ReactiveCrudRepository<Linha,Long> {
	Flux<Linha> findByCodigo(Mono<String> codigo);
	Flux<Linha> findByNome(Mono<String> nome);
}
