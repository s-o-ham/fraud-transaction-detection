import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.*;
import java.net.*;

public class Server {

    static String GEMINI_API_KEY = "API_KEY_HERE";

    public static void main(String[] args) throws Exception {

        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isEmpty()) {
            System.out.println(" GEMINI_API_KEY not set");
            return;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        server.createContext("/classify", exchange -> {
            try {
                System.out.println("\n Incoming request: " + exchange.getRequestMethod());

                if (enableCORS(exchange)) return;

                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    System.out.println(" Invalid HTTP method");
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                String body = new String(exchange.getRequestBody().readAllBytes());
                System.out.println(" Request Body: " + body);

                String message = extractValue(body, "message");
                System.out.println(" Extracted Message: " + message);

                if (message.isEmpty()) {
                    System.out.println(" Message extraction failed");
                    sendJson(exchange, 400, "{ \"error\": \"Message is empty\" }");
                    return;
                }

                System.out.println(" Calling Gemini API...");
                String result = callGemini(message);

                System.out.println(" Gemini Result: " + result);

                String jsonResponse =
                        "{ \"result\": \"" + escapeJson(result) + "\" }";

                sendJson(exchange, 200, jsonResponse);
                System.out.println(" Response sent successfully");

            } catch (Exception e) {
                System.out.println(" Exception in /classify handler");
                e.printStackTrace();
                sendJson(exchange, 500, "{ \"error\": \"Internal Server Error\" }");
            }
        });

        System.out.println(" Server running at http://localhost:8001/classify");
        server.start();
    }

    // -------------------- CORS --------------------
    private static boolean enableCORS(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                System.out.println(" CORS preflight request");
                exchange.sendResponseHeaders(200, -1);
            } catch (IOException ignored) {}
            return true;
        }
        return false;
    }

    // -------------------- SEND JSON --------------------
    private static void sendJson(HttpExchange exchange, int status, String json)
            throws IOException {

        System.out.println(" Sending JSON Response (" + status + "): " + json);

        byte[] bytes = json.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // -------------------- SIMPLE JSON PARSER --------------------
    private static String extractValue(String json, String key) {
        System.out.println(" Extracting key: " + key);

        String pattern = "\"" + key + "\"";
        int start = json.indexOf(pattern);
        if (start == -1) {
            System.out.println(" Key not found: " + key);
            return "";
        }

        start = json.indexOf(":", start) + 1;
        start = json.indexOf("\"", start) + 1;
        int end = json.indexOf("\"", start);

        if (end == -1) {
            System.out.println(" Closing quote not found for key: " + key);
            return "";
        }

        String value = json.substring(start, end);
        System.out.println(" Extracted value: " + value);
        return value;
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n");
    }

    // -------------------- GEMINI API CALL --------------------
    private static String callGemini(String userMessage) throws Exception {

        URL url = new URL(
            "https://generativelanguage.googleapis.com/v1beta/models/" +
            "gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY
        );

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String prompt =
            "Classify this SMS strictly as 'Fraud' or 'Not Fraud'. " +
            "Respond with only one word.\nMessage: " + userMessage;

        String requestJson =
            "{ \"contents\": [ { \"parts\": [ { \"text\": \"" +
            escapeJson(prompt) +
            "\" } ] } ] }";

        System.out.println(" Gemini Request JSON:\n" + requestJson);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestJson.getBytes());
        }

        int status = conn.getResponseCode();
        System.out.println(" Gemini HTTP Status: " + status);

        InputStream is = (status >= 400)
                ? conn.getErrorStream()
                : conn.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        System.out.println(" Raw Gemini Response:\n" + response);

        String output = extractValue(response.toString(), "text");

        if (output.isEmpty()) {
            System.out.println(" Gemini text extraction failed");
            return "Unknown";
        }

        return output;
    }
}
