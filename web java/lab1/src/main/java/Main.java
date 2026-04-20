import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;


public class Main {
    public static LinkedList<PointRecord> history = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        FCGIInterface fcgiInterface = new FCGIInterface();
        System.err.println("precess started");
        System.err.flush();
        while (fcgiInterface.FCGIaccept() >= 0){
            long startTime = System.currentTimeMillis();
            try {
                // прочитать тело, спарсить параметры
                String query = FCGIInterface.request.params.getProperty("QUERY_STRING");
                if (query.contains("history")){
                    sendJson("{\"history\":" + history.stream()
                            .map(PointRecord::toJson)
                            .collect(Collectors.joining(",", "[", "]"))
                            + "}");
                }
                else {
                    HashMap<String, String> params = parseQuery(query);


                    double x = Double.parseDouble(params.get("x"));
                    double y = Double.parseDouble(params.get("y"));
                    double r = Double.parseDouble(params.get("r"));
                    // провести работу по проверке
                    if (Validator.validate(x, y, r)) {
                        // высчитать время, проверить попадание, сформировать запись
                        boolean hit = ZoneChecker.check(x, y, r);
                        long currentTime = System.currentTimeMillis();
                        PointRecord record = new PointRecord(x, y, r, hit,
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").
                                        format(java.time.LocalDateTime.now()),
                                currentTime - startTime);
                        history.addFirst(record);
                        // отдать ответ в поток
                        sendJson(record.toJson());
                    } else {
                        throw new Exception("validation fails");
                    }
                }
            }catch (Exception e) {
                sendJson(String.format("{\"error\": \"%s\"}", e));
            }
        }
    }

    private static void sendJson(String jsonDump) {

        String response = String.format(
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: %d\r\n" +
                        "Connection: close\r\n\r\n%s",
                jsonDump.getBytes(StandardCharsets.UTF_8).length,
                jsonDump
        );
        System.out.print(response);
        System.out.flush();
    }

    private static HashMap<String, String> parseQuery(String query) {
        HashMap<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return map;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2); // важно ограничить на 2 части
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    : "";
            map.put(key, value);
        }
        return map;
    }
}
