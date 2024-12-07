import pokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Carbink("карабинчик", 55);
        Pokemon p2 = new Gligar("гилгарик", 80);
        Pokemon p3 = new Gliscor("глиссорчик", 80);
        Pokemon p4 = new Bellsprout("беллспортик", 29);
        Pokemon p5 = new Weepinbell("випинбеллчик", 29);
        Pokemon p6 = new Victreebel("виктребельчик", 29);
        b.addAlly(p1);
        b.addFoe(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addAlly(p5);
        b.addFoe(p6);
        b.go();
    }
}

