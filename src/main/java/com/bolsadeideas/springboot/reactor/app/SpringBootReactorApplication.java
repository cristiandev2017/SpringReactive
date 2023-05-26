package com.bolsadeideas.springboot.reactor.app;

import com.bolsadeideas.springboot.reactor.app.models.Usuario;
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
        Flux <Usuario> nombres = Flux.just("Cristian", "Andres","Maria", "Pedro", "Diego")
                .map(nombre ->  new Usuario(nombre.toUpperCase(), null))
                .doOnNext(usuario ->{
                    if (usuario == null){
                        throw new RuntimeException("Los nombres no pueden estar pelados");
                    }
                      System.out.println(usuario.getNombre());
                }).map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });


        nombres.subscribe(e -> log.info(e.getNombre()),
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
