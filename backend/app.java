import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter what you want to do (login/signin):");
        String answer = sc.next();

        if (answer.equalsIgnoreCase("signin")) {
            Authentication.Signin();
        }
        else if (answer.equalsIgnoreCase("login")) {
            Authentication.login();
        }
        else {
            System.out.println("Invalid input.");
        }
    }
}x