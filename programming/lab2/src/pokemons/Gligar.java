package pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import movement.*;

public class Gligar extends Pokemon {
    public Gligar(String name, int lvl){
        super(name, lvl);
        super.setType(Type.GROUND, Type.FLYING);
        super.setStats(65, 75, 105, 35, 65, 85);
        super.setMove(new AerialAce(), new Venoshock(), new Roost());
    }
}
