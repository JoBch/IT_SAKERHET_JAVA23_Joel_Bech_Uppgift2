import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerRequests {

    private static final String BASE_URL = "http://localhost:8080";


    static String sendPostRequest(String endpoint, String requestBody, String token) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        //Add authorization header only if the token is not null
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        } else conn.setRequestProperty("Authorization", "" + null);

        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        return handleResponse(conn);
    }

    static String sendGetRequest(String endpoint, String token) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        //Add authorization header only if the token is not null
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        } else conn.setRequestProperty("Authorization", "" + null);


        int responseCode = conn.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        return handleResponse(conn);
    }

    //Handle the response and return the body as a string
    private static String handleResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response from server: " + response);
            return response.toString();
        }
    }

}
