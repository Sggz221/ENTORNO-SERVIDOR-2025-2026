package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Harry implements Sorcerer {
    private List<Spell> spells = new ArrayList<Spell>();

    public void addSpell(Spell spell) {
        this.spells.add(spell);
    }

    public Integer checkSize() {
        return this.spells.size();
    }
}
