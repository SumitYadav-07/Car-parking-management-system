package backend;

import java.util.HashMap;
import java.util.Scanner;
public class Authentication{

    public static void Signin(HashMap<String,String> user_details){
        Scanner input =new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username=input.next();
        System.out.print("Enter your password: ");
        String password=input.next();
        if(user_details.containsKey(username)){
            System.out.println("Username already exist choose different username.");
            Signin(user_details);
            input.close();
            return;
        }

        user_details.put(username,password);

        System.out.println("Sign in successful.");
        input.close();
    }


    public static void login(HashMap<String,String> user_details){
        Scanner sca=new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username=sca.next();
        System.out.print("Enter your password: ");
        String password=sca.next();
        if(user_details.containsKey(username)){
            if(password.equals(user_details.get(username))){
                System.out.print("login successful.");
            }
            System.out.print("Invalid password");
        }
        System.out.println("Invalid Username.");
        sca.close();
    }
    
    
}
