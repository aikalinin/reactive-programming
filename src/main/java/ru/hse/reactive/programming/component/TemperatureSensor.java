package ru.hse.reactive.programming.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.hse.reactive.programming.model.Temperature;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Component
public class TemperatureSensor {
    private final ApplicationEventPublisher publisher;

    private final Random rnd = new Random();
    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void startProcessing() {
        this.executor.schedule(this::probe, 1, SECONDS);
    }

    private void probe() {
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));

        log.info("Current temperature {}", temperature);
        // запланировать следующее чтение спустя
        // случайное число секунд (от 0 до 5)
        executor.schedule(this::probe, rnd.nextInt(5000), MILLISECONDS);
    }
}
