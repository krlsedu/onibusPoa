package br.com.krlsedu.onibusPoa.service;

import br.com.krlsedu.onibusPoa.configs.LinhaCriadaEvent;
import br.com.krlsedu.onibusPoa.model.Intinerario;
import br.com.krlsedu.onibusPoa.model.Linha;
import br.com.krlsedu.onibusPoa.repository.IntinerarioRepository;
import br.com.krlsedu.onibusPoa.repository.LinhaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LinhaService {
	
	private final LinhaRepository linhaRepository;
	private final IntinerarioRepository intinerarioRepository;
	private final ApplicationEventPublisher publisher;
	
	@Autowired
	public LinhaService(LinhaRepository linhaRepository, IntinerarioRepository intinerarioRepository, ApplicationEventPublisher publisher) {
		this.linhaRepository = linhaRepository;
		this.intinerarioRepository = intinerarioRepository;
		this.publisher = publisher;
	}
	
	public Mono<Linha> salva(Linha linha) {
		Mono<Linha> linhaMono = linhaRepository.findByCodigo(Mono.just(linha.getCodigo()));
		Linha block = linhaMono.block();
		if (block == null) {
			return linhaRepository.save(linha).doOnNext(l -> this.publisher.publishEvent(new LinhaCriadaEvent(l)));
		} else {
			if (!block.equals(linha)) {
				return linhaRepository.save(linha);
			} else {
				return Mono.just(linha);
			}
		}
	}
	
	public Flux<Linha> salvaTodos(List<Linha> linhas) {
		return linhaRepository.saveAll(linhas).doOnNext(l -> this.publisher.publishEvent(new LinhaCriadaEvent(l)));
	}
	
	public Flux<Linha> buscaTodos() {
		return linhaRepository.findAll();
	}
	
	public Mono<Linha> buscaPorCodigo(String codigo) {
		return linhaRepository.findByCodigo(Mono.just(codigo));
	}
	
	public Flux<Linha> buscaPorLocalizacao(Point p, Distance d) {
		Flux<Intinerario> intinerarioFlux = intinerarioRepository.findByLocationNear(p, d);
		List<String> codigos = new ArrayList<>();
		List<Linha> linhas = new ArrayList<>();
		List<Intinerario> intinerarios = intinerarioFlux.collectList().block();
		assert intinerarios != null;
		for (Intinerario intinerario :
				intinerarios) {
			if (!codigos.contains(intinerario.getLinha().getCodigo())) {
				codigos.add(intinerario.getLinha().getCodigo());
				linhas.add(linhaRepository.findByCodigo(Mono.just(intinerario.getLinha().getCodigo())).block());
			}
		}
		return Flux.fromIterable(linhas);
	}
	
	public Flux<Linha> buscaPorNome(String nome) {
		return linhaRepository.findByNome(Mono.just(nome));
	}
	
	public Mono<Void> deletePorCodigo(String codigo) {
		return linhaRepository.deleteAll(buscaPorCodigo(codigo));
	}
}
