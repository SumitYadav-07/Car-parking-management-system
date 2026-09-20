import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Scanner;
public class Authentication{

    public static void Signin(HashMap<String,String> user_details){
        Scanner sc =new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username=sc.next();
        System.out.print("Enter your password: ");
        String password=sc.next();
        if(user_details.containsKey(username)){
            System.out.println("Username already exist choose different username.");
            Signin(user_details);
            return;
        }

        user_details.put(username,password);

        System.out.println("Sign in successful.");
    }

    public static void login(HashMap<String,String> user_details){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username=sc.next();
        System.out.print("Enter your password: ");
        String password=sc.next();
        if(user_details.containsKey(username)){
            if(password.equals(user_details.get(username))){
                System.out.print("login successful.");
            }
            System.out.print("Invalid password");
        }
        System.out.println("Invalid Username.");
    }
}
