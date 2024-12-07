package movement;

import ru.ifmo.se.pokemon.*;

public class Acid extends PhysicalMove {
    public Acid(){
        super(Type.POISON, 40, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        Effect acid = new Effect().chance(0.1).turns(-1).stat(Stat.DEFENSE, -1);
        p.addEffect(acid);
    }
    @Override
    protected String describe() {
        return "использует Кислоту";
    }
}
