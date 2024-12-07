public class HiderDidNotHear extends Exception {
    public HiderDidNotHear(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return String.format("Коротыш %s смог сопротивляться голосу и не вышел из укрытия", super.getMessage());
    }
}
