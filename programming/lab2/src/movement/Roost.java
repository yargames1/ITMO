package movement;

import ru.ifmo.se.pokemon.*;

public class Roost extends StatusMove {
    public Roost(){
        super.type = Type.FLYING;
    }
    @Override
    protected void applySelfEffects(Pokemon p){
        super.applySelfEffects(p);
        int hp = (int) (p.getStat(Stat.HP)*0.5 > p.getHP() ? 0.5*p.getStat(Stat.HP) : p.getStat(Stat.HP) - p.getHP());
        Effect RoostEffect = new Effect().chance(1).turns(0).stat(Stat.HP, hp);
        p.addEffect(RoostEffect);
    }
    @Override
    protected String describe() {
        return "использует Насест";
    }
}
