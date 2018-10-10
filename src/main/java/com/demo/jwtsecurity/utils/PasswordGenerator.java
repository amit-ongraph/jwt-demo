package com.demo.jwtsecurity.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class PasswordGenerator {

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder(5);
        String plainText;
        System.out.println("Enter your plain Text : ");
        Scanner input = new Scanner(System.in);
        plainText = input.next();

        System.out.println("Generated Password : " + encoder.encode(plainText));

    }
}
