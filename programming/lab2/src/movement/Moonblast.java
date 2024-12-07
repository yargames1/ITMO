package movement;

import ru.ifmo.se.pokemon.*;

public class Moonblast extends SpecialMove {
    public Moonblast() {
        super(Type.FAIRY, 95, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        Effect moonblastEffect = new Effect().chance(0.3).turns(-1).stat(Stat.ATTACK, -1);
        p.addEffect(moonblastEffect);
    }

    @Override
    protected String describe() {
        return "использует Лунный взрыв";
    }
}
