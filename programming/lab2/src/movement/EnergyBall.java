package movement;

import ru.ifmo.se.pokemon.*;

public class EnergyBall extends SpecialMove {
    public EnergyBall(){
        super(Type.GRASS, 90, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        Effect energyBall = new Effect().chance(0.1).turns(-1).stat(Stat.DEFENSE, -1);
        p.addEffect(energyBall);
    }
    @Override
    protected String describe() {
        return "использует Энергетический шар";
    }
}
