package movement;

import ru.ifmo.se.pokemon.*;

public class RockTomb extends  PhysicalMove{
    public RockTomb() {
        super(Type.ROCK, 60, 95);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        Effect rockTombEffect = new Effect().turns(-1).stat(Stat.SPEED, -1);
        p.addEffect(rockTombEffect);
    }
    @Override
    protected String describe() {
        return "использует Каменную гробницу";
    }
}
