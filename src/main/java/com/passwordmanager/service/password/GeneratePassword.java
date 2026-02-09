package com.passwordmanager.service.password;

import java.security.SecureRandom;
import java.util.Scanner;

public class GeneratePassword {

    // Generates a password based on length and selected character types
    public static String generatePassword(
            int length,
            boolean useUpper,
            boolean useLower,
            boolean useDigits,
            boolean useSymbols) {

        String characters = "";

        // Add uppercase letters if selected
        if (useUpper) {
            characters =
                    characters +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }

        // Add lowercase letters if selected
        if (useLower) {
            characters =
                    characters +
                    "abcdefghijklmnopqrstuvwxyz";
        }

        // Add digits if selected
        if (useDigits) {
            characters =
                    characters +
                    "0123456789";
        }

        // Add symbols if selected
        if (useSymbols) {
            characters =
                    characters +
                    "!@#$%^&*()-_=+<>?/{}~";
        }

        // If no character group was selected, return null
        if (characters.length() == 0) {
            return null;
        }

        SecureRandom random =
                new SecureRandom();

        String password = "";

        // Pick random characters to form password
        for (int i = 0; i < length; i++) {
            int index =
                    random.nextInt(
                            characters.length());

            password =
                    password +
                    characters.charAt(index);
        }

        return password;
    }

    // Console based password generator for manual testing
    public static void generatePasswordConsole() {

        // Scanner to take input from user
        Scanner scan = new Scanner(System.in);

        int length = 0;
        boolean upper = false;
        boolean lower = false;
        boolean digits = false;
        boolean symbols = false;

        try {

            // Ask user for password length
            System.out.println(
                    "Enter desired password length:");
            length =
                    Integer.parseInt(
                            scan.nextLine());

            // Ask which character sets to include
            System.out.println(
                    "Use Uppercase Letters? (y/n):");
            upper =
                    scan.nextLine()
                        .equalsIgnoreCase("y");

            System.out.println(
                    "Use Lowercase Letters? (y/n):");
            lower =
                    scan.nextLine()
                        .equalsIgnoreCase("y");

            System.out.println(
                    "Use Digits? (y/n):");
            digits =
                    scan.nextLine()
                        .equalsIgnoreCase("y");

            System.out.println(
                    "Use Symbols? (y/n):");
            symbols =
                    scan.nextLine()
                        .equalsIgnoreCase("y");

        } catch (Exception e) {
            System.out.println("Invalid input");
            return;
        }

        // Generate password using selected options
        String password =
                generatePassword(
                        length,
                        upper,
                        lower,
                        digits,
                        symbols);

        if (password != null) {
            System.out.println(
                    "Generated Password: " +
                    password);
        } else {
            System.out.println(
                    "No character type selected. Password not generated.");
        }
    }
}
