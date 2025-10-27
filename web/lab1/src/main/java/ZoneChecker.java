
public class ZoneChecker {
    public static boolean check(double x, double y, double r){
        boolean rec = (x >= 0 & x <= r/2) & (y >= 0 & y <= r);
        boolean cir = (x >= 0 & y <= 0) & (x*x + y*y <= r*r/4);
        boolean tri = (x <= 0 & y <= 0) & (y >= -x - r);
        return rec | cir | tri;
    }

}
