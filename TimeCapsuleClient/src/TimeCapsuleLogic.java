import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Scanner;

public class TimeCapsuleLogic {

    //Create a new time capsule
    static void createTimeCapsule(Scanner scanner) throws IOException {
        System.out.print("Enter your message: ");
        String message = scanner.nextLine();

        String requestBody = String.format("{\"message\": \"%s\"}", message);
        ServerRequests.sendPostRequest("/timecapsule/create", requestBody, Main.jwtToken);
        System.out.println(Main.jwtToken);
    }

    //View all time capsules for the logged-in user
    static void viewTimeCapsules() throws IOException {
        String response = ServerRequests.sendGetRequest("/timecapsule/view", Main.jwtToken);

        JsonElement jsonElement = JsonParser.parseString(response);

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            System.out.println("Time Capsules:");
            for (JsonElement element : jsonArray) {
                System.out.println("- " + element.getAsString());
            }
        } else System.out.println("Unexpected response format: " + response);
    }

}
