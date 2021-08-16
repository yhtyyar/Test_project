package main.java.com;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Исходная задача: Существует два потока(thread) - producer и consumer.
 * Producer - должен дописывать в конец randoms случайное число случайных чисел,
 * Consumer - должен считывать из randoms все числа и удалять их оттуда, выводя при этом их на печать
 * Требуется обеспечить потокобезопасность данных с помощью синхронизации.
 * Потоки должны работать строго последовательно, и не должно быть ситуаций,
 * когда один поток отрабатывал бы несколько раз подряд, а другой простаивал
 * В примере представлен кусок "Грязного" кода, в котором допущен ряд ошибок
 * <p>
 * Требуется выполнить рефакторинг кода (переименовать плохо названные объекты/методы/классы,
 * обработать критические секции кода, выполнить оптимизацию и прочее),
 * а также доработать программу таким образом, чтобы исходная задача была выполнена.
 * <p>
 * Желательные условия: дополнительным плюсом будет считаться реализация
 * всех инфраструктурных подсистем, таких как логирование и чтение параметров из файлов конфигурации
 * Решение задачи необходимо отправить ссылкой на опубликованный проект на github.com
 */
public class ThreadSafeWithSynchronization {

    private final ReentrantLock lock = new ReentrantLock();
    private final List<AtomicInteger> randomNumbers = new ArrayList<>();
    private int count = 0;

    public void startThreadProcess() {
        PRODUCER.start();
        CONSUMER.start();
    }

    private final Thread PRODUCER = new Thread(() -> {

        lock.lock();
        count++;

        if (count == 1) {
            for (int i = 0; i < new Random().nextInt(100); i++) {

                AtomicInteger number = new AtomicInteger();

                number.set(new Random().nextInt(100));
                randomNumbers.add(number);
            }
            System.out.println("Numbers written");
        } else {
            count--;
        }


        lock.unlock();
    });


    // Поток для чтения данных. Читает данные из списка Randoms и выводит их в консоль
    private final Thread CONSUMER = new Thread(() -> {

        lock.lock();
        count++;

        if (count == 2) {
            System.out.println("Reading numbers");

            randomNumbers.forEach(System.out::println);
            randomNumbers.clear();
        } else {
            count--;
        }

        lock.unlock();
    });
}

