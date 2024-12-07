package pokemons;

import movement.*;
import ru.ifmo.se.pokemon.*;

public class Weepinbell extends Bellsprout {
    public Weepinbell(String name, int lvl) {
        super(name, lvl);
        super.setType(Type.GRASS, Type.POISON);
        super.setStats(65, 90, 50, 85, 45, 55);
        super.setMove(new SweetScent(), new EnergyBall(), new Acid());
    }
}
