package com.bolsadeideas.springboot.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Flux <String> nombres = Flux.just("Cristian", "Andres","Maria", "Pedro", "Diego")
                .doOnNext(elemento ->{
                    if (elemento.isEmpty()){
                        throw new RuntimeException("Los nombres no pueden estar estan pelados");
                    }
                      System.out.println(elemento);
                });

        nombres.subscribe(log::info,
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecucion del observable con exito");
                    }
                }
        );
    }
}
