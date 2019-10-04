package br.com.krlsedu.onibusPoa.service;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.repository.IntinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class IntinerarioService {
	
	private final IntinerarioRepository intinerarioRepository;
	
	@Autowired
	public IntinerarioService(IntinerarioRepository intinerarioRepository, ApplicationEventPublisher publisher) {
		this.intinerarioRepository = intinerarioRepository;
	}
	
	public Mono<Intinerario> salva(Intinerario intinerario) {
		Mono<Intinerario> mono = intinerarioRepository.findByPonto(Mono.just(intinerario.getPonto()));
		Intinerario block = mono.block();
		if (block == null) {
			return intinerarioRepository.save(intinerario);
		} else {
			if (!block.equals(intinerario)) {
				return intinerarioRepository.save(intinerario);
			} else {
				return Mono.just(intinerario);
			}
		}
	}
	
	public Flux<Intinerario> salvaTodos(List<Intinerario> intinerarios) {
		return intinerarioRepository.saveAll(intinerarios);
	}
	
	public Flux<Intinerario> buscaTodos() {
		return intinerarioRepository.findAll();
	}
	
	public Mono<Intinerario> buscaPorPonto(String ponto) {
		return intinerarioRepository.findByPonto(Mono.just(ponto));
	}
	
	public Mono<Void> deletePorPonto(String ponto) {
		return intinerarioRepository.deleteAll(buscaPorPonto(ponto));
	}
}
