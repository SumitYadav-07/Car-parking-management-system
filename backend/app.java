package backend;

import java.util.Scanner;
import java.util.HashMap;
public class App{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String,String> user_details=new HashMap<>();

        System.out.println("Enter what you want to do (login/signin):");
        String answer = sc.next();
        if (answer.equalsIgnoreCase("signin")) {
            Authentication.Signin(user_details);
        }
        else if (answer.equalsIgnoreCase("login")) {
            Authentication.login(user_details);
        }
        else {
            System.out.println("Invalid input.");
        }
        sc.close();
    }
}