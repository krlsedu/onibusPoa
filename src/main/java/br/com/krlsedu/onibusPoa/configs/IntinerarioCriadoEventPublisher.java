package br.com.krlsedu.onibusPoa.configs;

import br.com.krlsedu.onibusPoa.model.Intinerario;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class IntinerarioCriadoEventPublisher implements ApplicationListener<IntinerarioCriadoEvent>, Consumer<FluxSink<Intinerario>> {
	
	private final Executor executor;
	private final BlockingQueue<IntinerarioCriadoEvent> queue = new LinkedBlockingQueue<>();
	
	public IntinerarioCriadoEventPublisher(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public void onApplicationEvent(IntinerarioCriadoEvent intinerarioCriadoEvent) {
		this.queue.offer(intinerarioCriadoEvent);
	}
	
	@Override
	public void accept(FluxSink<Intinerario> intinerarioFluxSink) {
		this.executor.execute(() -> {
			while (true)
				try {
					IntinerarioCriadoEvent event = queue.take();
					intinerarioFluxSink.next(event.getIntinerario());
				} catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
		});
	}
}
