package br.com.krlsedu.onibusPoa.configs;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class IntinerarioCriadoEvent extends ApplicationEvent {
	
	@Getter
	private final Intinerario intinerario;
	
	public IntinerarioCriadoEvent(Intinerario intinerario) {
		super(intinerario);
		this.intinerario = intinerario;
	}
	
}
