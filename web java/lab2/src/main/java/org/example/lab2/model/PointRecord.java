package org.example.lab2.model;

public class PointRecord {
    // Записываем точку. Для того чтобы кинуть в список потом
    private final double x;
    private final double y;
    private final double r;
    private final boolean hit;
    private final String serverTimeMillis;      // время на сервере в момент обработки
    private final long processingTimeMillis;  // сколько занял обработчик (ms)


    public PointRecord(double x, double y, double r, boolean hit,
                       String serverTimeMillis, long processingTimeMillis) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.serverTimeMillis = serverTimeMillis;
        this.processingTimeMillis = processingTimeMillis;
    }

    public String toJson() {
        return String.format(
                "{\"x\":%s,\"y\":%s,\"r\":%s,\"hit\":%s,\"serverTime\":\"%s\",\"procTime\":%d}",
                x, y, r, hit, serverTimeMillis, processingTimeMillis
        );
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public boolean isHit() { return hit; }
    public String getServerTimeMillis() { return serverTimeMillis; }
    public long getProcessingTimeMillis() { return processingTimeMillis; }

}
