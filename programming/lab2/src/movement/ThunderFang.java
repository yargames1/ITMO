package movement;

import ru.ifmo.se.pokemon.*;

public class ThunderFang extends PhysicalMove{
    public ThunderFang(){
        super(Type.ELECTRIC, 65, 95);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        Effect.flinch(p);
        Effect thunderFangEffect = new Effect().chance(0.1).condition(Status.PARALYZE);
        p.addEffect(thunderFangEffect);
    }
}
