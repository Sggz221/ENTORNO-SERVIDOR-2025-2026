package org.example;

import org.example.models.*;
import reactor.core.publisher.Flux;

import java.time.Duration;


public class Main {
    public static void main(String[] args) {

        //Instanciamos los personajes
        Voldemort voldemort = new Voldemort();
        Harry harry = new Harry();
        Hermione hermione = new Hermione();
        Ron ron = new Ron();

        //Instanciamos el flujo de hechizos para que emita cada 500 milisegundos
        Flux<Spell> flux = Flux.interval(Duration.ofMillis(500))
                //Lo mapeamos a Flux<Spell>
                .map(spell -> voldemort.castSpell())
                //Establecemos la condición de salida del flujo
                .takeUntil(number -> {
                        if (harry.checkSize() >= 3){
                            System.out.println("FIN DEL JUEGO ¡Harry ha interceptado 3 hechizos!");
                            return true;
                        }
                        if (hermione.checkSize() >= 5){
                            System.out.println("FIN DEL JUEGO ¡Hermione ha interceptado 5 hechizos!");
                            return true;
                        }
                        if (ron.checkSize() >= 2){
                            System.out.println("FIN DEL JUEGO ¡Ron ha interceptado 5 hechizos!");
                            return true;
                        }
                        return false;
                })
                //Convierte el flujo en compartido, para que todos los suscriptores reciban los mismos eventos
                .share();

        //Harry se suscribe desde el primer momento que el flujo empieza a emitir
        System.out.println("Harry se suscribe al flujo de hechizos");

        flux.subscribe(spell -> {
            if (spell.getTipo() == Tipo.ATAQUE) {
                harry.addSpell(spell);
                System.out.println("Harry ha interceptado " + harry.checkSize() + " hechizos de ATAQUE");
            }
        });

        try {
            Thread.sleep(1000); // Esperamos 1 segundo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Hermione se suscribe al primer segundo
        System.out.println("Hermione se suscribe al flujo de hechizos");

        flux.subscribe(spell -> {
            if (spell.getTipo() == Tipo.CURACION) {
                hermione.addSpell(spell);
                System.out.println("Hermione ha interceptado " + hermione.checkSize() + " hechizos de CURACIÓN");

            }
        });

        try {
            Thread.sleep(1000); // Esperamos 1 segundo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Ron se suscribe al segunfo segundo
        System.out.println("Ron se suscribe al flujo de hechizos");

        flux.subscribe(spell -> {
            if (spell.getTipo() == Tipo.ESQUIVAR) {
                ron.addSpell(spell);
                System.out.println("Ron ha interceptado " + ron.checkSize() + " hechizos de ESQUIVAR");

            }
        });

        //Necesario para que el hilo principal (por el que corre main() no se termine
        //hasta que la condición del takeUntil se cumpla
        flux.blockLast();

    }
}
