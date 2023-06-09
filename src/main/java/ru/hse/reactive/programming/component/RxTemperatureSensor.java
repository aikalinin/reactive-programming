package ru.hse.reactive.programming.component;

import org.springframework.stereotype.Component;
import ru.hse.reactive.programming.model.Temperature;
import rx.Observable;

import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class RxTemperatureSensor {
    private final Random rnd = new Random();

    /**
     * Temperature values "stream" pipeline
     */
    private final Observable<Temperature> dataStream = Observable
            .range(0, Integer.MAX_VALUE)        // генерирует последовательность целых чисел от 0 до Integer.MAX_VALUE
            .concatMap(tick -> Observable           // Преобразует каждое сгенерированное число в другой Observable с помощью функции concatMap()
                    .just(tick)                     // Превращает текущий объект в Observable, который возвращает (эмитит/отправляет) ровно одно значение
                    .delay(rnd.nextInt(5000), MILLISECONDS) // Ждём
                    .map(tickValue -> this.probe()) // Генерируем значение температуры
            )                                       // Итого: на каждое число получаем Observable, который emit'ит ровно 1 рандомное значение температуры
            .publish()                              // Трансформация ConnectableObservable, который будет emit'ить только после команды connect()
                                                    // вызов connect() предполагает, что на него подписалось достаточное количество слушателей
            .refCount();                            // вызывает connect(), когда есть хотя бы 1 подписчик

    /**
     * Generates temperature (emulates thermometer)
     */
    private Temperature probe() {
        return new Temperature(16 + rnd.nextGaussian() * 10);
    }

    /**
     * Getter
     */
    public Observable<Temperature> getDataStream() {
        return dataStream;
    }
}
