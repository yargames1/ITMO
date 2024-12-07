package movement;

import ru.ifmo.se.pokemon.*;

public class PowerGem extends SpecialMove {
    public PowerGem() {
        super(Type.ROCK, 80, 100);
    }

    @Override
    protected String describe() {
        return "использует Силу самоцвета";
    }
}
