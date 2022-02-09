package org.example;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class bankRepository {

    static Connection connection = App.connection;
    static Scanner s = new Scanner(System.in);

    public static boolean createAccount(String customer_Name, int pass_code) {

        try {

            if (customer_Name.equals("") || pass_code == 0) {
                System.out.println("Invalid arguments");
                return false;
            }

            int ac = new Random().nextInt(1000);
            System.out.println("Enter the amount you want to deposit");
            int amount = s.nextInt();
            Statement statement = connection.createStatement();
            String query = "INSERT INTO customer values(" + ac + ", '" + customer_Name + "', " + amount + " ," + pass_code + ")";

            if (statement.executeUpdate(query) == 1) {
                System.out.println(customer_Name + ", You can login Now!");
                System.out.println("Your account number is " + ac);
                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Invalid Username");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean loginAccount(String customer_name, int passCode) {

        try {

            if (customer_name.equals("") || passCode == 0) {
                System.out.println("Invalid arguments");
                return false;
            }

            String query = "SELECT * from customer where customer_name=" + "'" + customer_name + "'" + " and pass_code=" + passCode;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int choice;
                int sender_account = resultSet.getInt("account_number");
                int receiver_account, amount;
                System.out.println("Welcome back, " + resultSet.getString("customer_name"));
                boolean flag = true;
                while (flag) {

                    try {

                        System.out.println("Enter 1 for money transfer");
                        System.out.println("Enter 2 for viewing balance");
                        System.out.println("Enter 0  to logout");

                        System.out.println("Enter your choice:");
                        choice = s.nextInt();
                        switch (choice) {

                            case 1:
                                System.out.println("Enter receiver's account number");
                                receiver_account = s.nextInt();
                                System.out.println("Enter the amount to be transferred");
                                amount = s.nextInt();

                                if (trasnferMoney(sender_account, receiver_account, amount)) {
                                    System.out.println("Money transferred successfully");
                                } else {
                                    System.out.println("Transaction failed");
                                }
                                break;
                            case 2:
                                viewBalance(sender_account);
                                break;
                            case 0:
                                System.out.println("Logging out!!!");
                                flag = false;
                                break;
                            default:
                                System.out.println("Invalid input");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                return false;
            }
        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println("Invalid User");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }


    private static void viewBalance(int sender_account) {

        try {

            String query = "SELECT * from customer where account_number=" + sender_account;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){

                System.out.println("Account Number: " + resultSet.getInt("account_number")
                        + "\nCustomer Name : " + resultSet.getString("customer_name")
                        + "\nBalance : " + resultSet.getString("balance"));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private static boolean trasnferMoney(int sender_account, int receiver_account, int amount) {

        if (receiver_account == 0 || amount == 0) {
            System.out.println("Invalid arguments");
            return false;
        }

        try {

            connection.setAutoCommit(false);
            String query = "SELECT * from customer where account_number=" + sender_account;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                if((resultSet.getInt("balance")) < amount){
                    System.out.println("Insufficient balance!");
                    return false;
                }
            }
            Statement statement = connection.createStatement();
            connection.setSavepoint();

            query = "update customer set balance=balance-" + amount + " where account_number=" + sender_account;
            if(statement.executeUpdate(query) == 1){

                System.out.println("Amount transferred");

            }

            query = "update customer set balance=balance+" + amount + " where account_number=" + receiver_account;
            statement.executeUpdate(query);

            connection.commit();
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}

