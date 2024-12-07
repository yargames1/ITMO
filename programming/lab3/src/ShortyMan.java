public abstract class ShortyMan implements CanMove {
    Location location;

    @Override
    public void moveTo(Location location) {
        this.location = Location.valueOf(location.name());
    }
}
