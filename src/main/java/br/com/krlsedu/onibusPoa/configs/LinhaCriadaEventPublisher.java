package br.com.krlsedu.onibusPoa.configs;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class LinhaCriadaEventPublisher implements
    ApplicationListener<LinhaCriadaEvent>, // <1>
    Consumer<FluxSink<LinhaCriadaEvent>> { //<2>

    private final Executor executor;
    private final BlockingQueue<LinhaCriadaEvent> queue =
        new LinkedBlockingQueue<>(); // <3>

    LinhaCriadaEventPublisher(Executor executor) {
        this.executor = executor;
    }

    // <4>
    @Override
    public void onApplicationEvent(LinhaCriadaEvent event) {
        this.queue.offer(event);
    }

     @Override
    public void accept(FluxSink<LinhaCriadaEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    LinhaCriadaEvent event = queue.take(); // <5>
                    sink.next(event); // <6>
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
