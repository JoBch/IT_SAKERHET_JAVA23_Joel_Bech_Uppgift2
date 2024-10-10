import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String jwtToken = null;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (true) {
            if (!loggedIn) {
                System.out.println("1. Login \n2. Register \n3. Exit \nChoose your option");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        loggedIn = login(scanner);
                        break;
                    case 2:
                        register(scanner);
                        break;
                    case 3:
                        System.exit(0);
                }
            } else {
                System.out.println("1. Create Time Capsule \n2. View Time Capsules \n3. Logout \nChoose an option: ");
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
            jwtToken = extractFieldFromJson(response);
            return true;
        } else return false;

    }

    private static String extractFieldFromJson(String json) {
        int startIndex = json.indexOf("token") + "token".length() + 3; //Finding the start of the field value
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }
}
