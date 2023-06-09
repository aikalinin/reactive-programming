package ru.hse.reactive.programming.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.hse.reactive.programming.component.RxTemperatureSensor;
import ru.hse.reactive.programming.model.RxSeeEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RxTemperatureController {

    private final RxTemperatureSensor temperatureSensor;

    /**
     * Subscribe a client to the temperature stream
     */
    @GetMapping("/rx-temperature-stream")
    public SseEmitter events(HttpServletRequest request) {
        RxSeeEmitter emitter = new RxSeeEmitter();

        temperatureSensor.getDataStream().subscribe(emitter.getSubscriber());

        return emitter;
    }
}
