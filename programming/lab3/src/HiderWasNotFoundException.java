public class HiderWasNotFoundException extends RuntimeException {
    public HiderWasNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return String.format("%s не был обнаружен, поэтому не будет выведен из павильона", super.getMessage());
    }
}
