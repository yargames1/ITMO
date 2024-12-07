package pokemons;

import movement.*;
import ru.ifmo.se.pokemon.Type;

public class Gliscor extends Gligar {
    public Gliscor(String name, int lvl) {
        super(name, lvl);
        super.setType(Type.GROUND, Type.FLYING);
        super.setStats(75, 95, 125, 45, 75, 95);
        super.setMove(new AerialAce(), new Venoshock(), new Roost(), new ThunderFang());
    }
}
