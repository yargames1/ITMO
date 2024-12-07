package pokemons;

import movement.*;
import ru.ifmo.se.pokemon.*;

public class Bellsprout extends Pokemon {
    public Bellsprout(String name, int lvl){
        super(name, lvl);
        super.setType(Type.GRASS, Type.POISON);
        super.setStats(50, 75, 35, 70, 30, 40);
        super.setMove(new SweetScent(), new EnergyBall());
    }
}
