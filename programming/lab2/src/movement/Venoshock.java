package movement;

import ru.ifmo.se.pokemon.*;

public class Venoshock extends SpecialMove {
    public Venoshock(){
        super(Type.POISON, 65, 100);
    }
    @Override
    protected String describe() {
        return "использует Веношок";
    }

    @Override
    protected void applyOppDamage(Pokemon pokemon, double damage) {
        int multiplier = pokemon.getCondition() == Status.POISON ? 2 : 1;
        super.applyOppDamage(pokemon, damage*multiplier);
    }
}


парастатическое и динамическое связывание