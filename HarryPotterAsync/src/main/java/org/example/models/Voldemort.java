package org.example.models;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Voldemort {
    public Spell castSpell(){
        Random rand = new Random();
        int numero = rand.nextInt(3) + 1; //Genera un número aleatorio entre 0 y 2, por eso hay que sumar 1
        Spell spell = new Spell();

        switch (numero){
            case 1: {
                spell.setTipo(Tipo.ATAQUE);
                break;
            }

            case 2: {
                spell.setTipo(Tipo.CURACION);
                break;
            }

            case 3: {
                spell.setTipo(Tipo.ESQUIVAR);
            }
        }

        return spell;
    };

    //Lista de hechizos que va lanzando Voldemort
    private final List<Spell> spells =  new ArrayList<Spell>();
    //Se crea un fluxSink, que luego será necesario pasarle como parámetro
    //de emisor a la función create() para crear el flujo de hechizos
    private FluxSink<List<Spell>> spellFluxSink;
    //Se crea el flujo de hechizos, el share() sirve paraq que todos los suscriptores
    //reciban los mismos eventos
    private final Flux<List<Spell>> spellFlux = Flux.<List<Spell>>create(emisor -> this.spellFluxSink = emisor).share();

    public void add(){

        Spell newSpell = castSpell();

        // Generamos el hechizo aleatorio y lo añadimos a la LISTA de hechizos
        spells.add(newSpell);

        // Se emite el evento con el nuevo hechizo ya añadido a la lista
        spellFluxSink.next(spells);
    }

}
