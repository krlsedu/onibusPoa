package br.com.krlsedu.onibusPoa.configs;

import br.com.krlsedu.onibusPoa.model.Linha;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class LinhaCriadaEventPublisher implements ApplicationListener<LinhaCriadaEvent>, Consumer<FluxSink<Linha>> {
    
    private final Executor executor;
    private final BlockingQueue<LinhaCriadaEvent> queue = new LinkedBlockingQueue<>();
    
    public LinhaCriadaEventPublisher(Executor executor) {
        this.executor = executor;
    }
    
    @Override
    public void onApplicationEvent(LinhaCriadaEvent linhaCriadaEvent) {
        this.queue.offer(linhaCriadaEvent);
    }
    
    @Override
    public void accept(FluxSink<Linha> linhaFluxSink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    LinhaCriadaEvent linhaCriadaEvent = queue.take();
                    linhaFluxSink.next(linhaCriadaEvent.getLinha());
                } catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
