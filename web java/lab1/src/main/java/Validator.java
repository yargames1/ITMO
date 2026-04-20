import java.util.Set;

public class Validator {
    private static final Set<Double> ALLOWED_X = Set.of(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);

    private static final Set<Double> ALLOWED_R = Set.of(1.0, 1.5, 2.0, 2.5, 3.0);

    public static boolean validate(double x, double y, double r){
        return validateX(x) && validateY(y) && validateR(r);
    }
    private static boolean validateY(double y){

        return -5 <= y && y <= 3;
    }
    private static boolean validateX(double x){
        return ALLOWED_X.contains(x);
    }
    private static boolean validateR(double r){
        return ALLOWED_R.contains(r);
    }
}
