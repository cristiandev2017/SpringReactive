package com.bolsadeideas.springboot.reactor.app;

import com.bolsadeideas.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
       // ejemploIterable();
       // ejemploFlatmap();
       //ejemplotoString();
       ejemplotoCollectList();
    }


    public void ejemploIterable() throws Exception {

        List <String> usuariosList = new ArrayList <>();
        usuariosList.add("Cristian Santamaria");
        usuariosList.add("Andres Guzman");
        usuariosList.add("Maria Santamaria");
        usuariosList.add("Pedro Sarmiento");
        usuariosList.add("Bruce Lee");
        usuariosList.add("Bruce Wily");

        Flux <String> nombres = Flux.fromIterable(usuariosList);


        Flux <Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Los nombres no pueden estar pelados");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                }).map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });


        usuarios.subscribe(e -> log.info(e.toString()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecucion del observable con exito");
                    }
                }
        );
    }

    public void ejemploFlatmap() {

        List <String> usuariosList = new ArrayList <>();
        usuariosList.add("Cristian Santamaria");
        usuariosList.add("Andres Guzman");
        usuariosList.add("Maria Santamaria");
        usuariosList.add("Pedro Sarmiento");
        usuariosList.add("Bruce Lee");
        usuariosList.add("Bruce Wily");

        Flux.fromIterable(usuariosList)
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .flatMap(usuario -> {
                    if (usuario.getNombre().equalsIgnoreCase("bruce")){
                        return Mono.just(usuario);
                    }else{
                        return Mono.empty();
                    }
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                }).subscribe(u ->log.info(u.toString())

        );
    }

    public void ejemplotoString() {

        List <Usuario> usuariosList = new ArrayList <>();
        usuariosList.add(new Usuario( "Cristian" ,"Santamaria"));
        usuariosList.add(new Usuario( "Andres", "Guzman"));
        usuariosList.add(new Usuario( "Maria", "Santamaria"));
        usuariosList.add(new Usuario( "Pedro", "Sarmiento"));
        usuariosList.add(new Usuario( "Bruce", "Lee"));
        usuariosList.add(new Usuario( "Bruce", "Wily"));

        Flux.fromIterable(usuariosList)
                .map(usuario -> (usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase())))
                .flatMap(nombre -> {
                    if (nombre.contains("bruce".toUpperCase())){
                        return Mono.just(nombre);
                    }else{
                        return Mono.empty();
                    }
                })
                .map(String::toLowerCase)
                .subscribe(u ->log.info(u.toString())
                );
    }

    public void ejemplotoCollectList(){

        List <Usuario> usuariosList = new ArrayList <>();
        usuariosList.add(new Usuario("Cristian", "Santamaria"));
        usuariosList.add(new Usuario("Andres", "Guzman"));
        usuariosList.add(new Usuario("Maria", "Santamaria"));
        usuariosList.add(new Usuario("Pedro", "Sarmiento"));
        usuariosList.add(new Usuario("Bruce", "Lee"));
        usuariosList.add(new Usuario("Bruce", "Wily"));

        Flux.fromIterable(usuariosList)
                .collectList()
                .subscribe(lista ->
                        lista.forEach(usuario -> log.info(usuario.toString()))
                        );
    }
}

