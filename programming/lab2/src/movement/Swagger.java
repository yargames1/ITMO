package movement;

import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger(){
        super(Type.NORMAL, 0, 85);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        p.confuse();
        Effect swaggerEffect = new Effect().turns(-1).stat(Stat.ATTACK, 2);
        p.addEffect(swaggerEffect);
    }
    @Override
    protected String describe() {
        return "использует Развязность";
    }
}
