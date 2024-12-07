public class Rock extends Item{

    private static boolean gravity_is_off = false;

    public Rock(){
        super.name = "Камень";
    }

    public static void changeGravityToLow(){
        gravity_is_off = true;
    }

    public static void changeGravityToHigh() {
        gravity_is_off = false;
    }

    public static boolean getGravityBolean(){
        return gravity_is_off;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Rock;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
