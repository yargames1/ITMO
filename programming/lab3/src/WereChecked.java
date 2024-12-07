public record WereChecked(ShortyHider ShortyHider, boolean was_found) {
    public WereChecked {
        if (!was_found) {
            throw new HiderWasNotFoundException(ShortyHider.toString());
            }
        }
}
