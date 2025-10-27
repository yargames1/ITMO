public class PointRecord {
    // Записываем точку. Для того чтобы кинуть в список потом
    public final double x;
    public final double y;
    public final double r;
    public final boolean hit;
    public final String serverTimeMillis;      // время на сервере в момент обработки
    public final long processingTimeMillis;  // сколько занял обработчик (ms)

    public PointRecord(double x, double y, double r, boolean hit,
                       String serverTimeMillis, long processingTimeMillis) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.serverTimeMillis = serverTimeMillis;
        this.processingTimeMillis = processingTimeMillis;
    }

    // JSON
    public String toJson() {
        return String.format(
                "{\"x\":%s,\"y\":%s,\"r\":%s,\"hit\":%s,\"serverTime\":\"%s\",\"procTime\":%d}",
                x, y, r, hit, serverTimeMillis, processingTimeMillis
        );
    }
}
