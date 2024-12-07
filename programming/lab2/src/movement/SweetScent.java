package movement;

import ru.ifmo.se.pokemon.*;

public class SweetScent extends StatusMove {
    public SweetScent(){
        super(Type.NORMAL, 0, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        Effect sweetScentEffect = new Effect().turns(-1).stat(Stat.EVASION, -1);
        p.addEffect(sweetScentEffect);
    }
    @Override
    protected String describe() {
        return "использует Сладкий аромат";
    }
}
