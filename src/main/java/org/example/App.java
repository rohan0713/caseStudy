package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class App 
{
    public static Connection connection;
    static Scanner s = new Scanner(System.in);

    public static void main( String[] args )
    {
        try {
            String mysqlDriver = "com.mysql.cj.jdbc.Driver";
            Class.forName(mysqlDriver);
            String username = "root";
            String password = "rohan123";
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", username, password);
            manage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void manage() {

        String customer_name = "";
        int password;
        int choice;
        boolean flag = true;
        while(flag){

            System.out.println("IBM bank welcomes you!");
            System.out.println("Enter 1 for Account Creation");
            System.out.println("Enter 2 for Account login");
            System.out.println("Enter 0 to exit");

            try {
                System.out.println("Enter your choice :");
                choice = s.nextInt();
                s.nextLine();

                switch (choice){

                    case 1 :
                        try {
                            System.out.println("Enter your name:");
                            customer_name = s.nextLine();
                            System.out.println("Create your password:");
                            password = s.nextInt();
                            s.nextLine();

                            if(bankRepository.createAccount(customer_name, password)){

                                System.out.println("Account created successfully");

                            }else{

                                System.out.println("Account creation failed!");
                            }

                        }
                        catch (Exception e){

                            System.out.println("Enter valid data please!");

                        }
                        break;

                    case 2 :
                        try {
                            System.out.println("Enter your name:");
                            customer_name = s.nextLine();
                            System.out.println("Enter your password:");
                            password =s.nextInt();

                            if(bankRepository.loginAccount(customer_name,password)){

                                System.out.println("Have a good day!");

                            }else{

                                System.out.println("Invalid credentials");
                            }
                        }
                        catch (Exception e){
                            System.out.println("Enter valid login credentials");
                        }
                        break;

                    case 0:
                        System.out.println("Visit us again!!");
                        flag = false;
                        break;

                    default:
                        System.out.println("Invalid input");
                }
            }catch (Exception e){

                System.out.println("Enter valid inputs");

            }

        }
    }
}
