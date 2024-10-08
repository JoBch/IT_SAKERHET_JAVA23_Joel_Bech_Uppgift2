import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String jwtToken = null;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (true) {
            if (!loggedIn) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    register(scanner);
                } else if (choice == 2) {
                    loggedIn = login(scanner);
                }
            } else {
                System.out.println("1. Create Time Capsule");
                System.out.println("2. View Time Capsules");
                System.out.println("3. Logout");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        TimeCapsuleLogic.createTimeCapsule(scanner);
                        break;
                    case 2:
                        TimeCapsuleLogic.viewTimeCapsules();
                        break;
                    case 3: {
                        jwtToken = null;
                        loggedIn = false;
                        System.out.println("Logged out!");
                    }
                }
            }
        }
    }

    //Register a new user
    private static void register(Scanner scanner) throws IOException {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String requestBody = String.format("{\"email\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}", email, username, password);
        ServerRequests.sendPostRequest("/users/register", requestBody, null);
    }

    //Login and retrieve JWT token
    private static boolean login(Scanner scanner) throws IOException {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        String response = ServerRequests.sendPostRequest("/users/login", requestBody, null);

        if (response.contains("token")) {
            jwtToken = extractFieldFromJson(response, "token");
            System.out.println("Login successful! JWT Token: " + jwtToken);
            return true;
        } else {
            System.out.println("Login failed! " + response);
            return false;
        }
    }

    private static String extractFieldFromJson(String json, String field) {
        int startIndex = json.indexOf(field) + field.length() + 3; // Finding the start of the field value
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }
}
