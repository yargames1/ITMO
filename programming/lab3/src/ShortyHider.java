import java.util.Objects;

public class ShortyHider extends ShortyMan{
    private String name;
    private double height;
    private double sanity = Math.random();
    private double stealth;
    private boolean mobility;

    public ShortyHider(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShortyHider s){
            return (Objects.equals(s.name, this.name)) && (s.sanity == this.sanity);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode()+(int)sanity*100;
    }

    @Override
    public String toString() {
        return String.format("Коротыш %s",this.name);
    }

    void hide(){
        this.height = Rock.getGravityBolean() ? Math.random() : 0.0f;
        this.stealth = Math.random();
        moveTo(Location.getRandomLocation());
    }
    public Location getLocation(){
        return this.location;
    }
    public double getStealth(){
        return stealth;
    }
    public boolean getMobility(){
        return mobility;
    }
     boolean listenToSeeker(double inculcation) throws HiderDidNotHear {
        if (inculcation > this.sanity) {
            System.out.printf("Коротыш %s не смог сопротивляться голосу и вышел самостоятельно\n", this.name);
            return true;
        }
         throw new HiderDidNotHear(this.name);
     }
     void fall(){
        if (!Rock.getGravityBolean()){
            if (height < 0.4f){
                System.out.printf("Коротыш %s не получил урона при падении\n", this.name);
                mobility = true;
            }
            else {
                System.out.printf("Коротыш %s получил урон при падении\n", this.name);
                mobility = false;
            }
            height = 0;
        }
     }
}
