package pokemons;

import movement.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Carbink extends Pokemon {
    public Carbink(String name, int lvl) {
        super(name, lvl);
        super.setType(Type.FAIRY, Type.ROCK);
        super.setStats(50, 50, 150, 50, 150, 50);
        super.setMove(new RockTomb(), new Moonblast(),
                new DazzlingGleam(), new PowerGem());
    }
}
