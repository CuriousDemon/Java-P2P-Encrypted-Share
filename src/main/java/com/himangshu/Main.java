package com.himangshu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. RECEIVE | 2. SEND");
        int choice = sc.nextInt();
        sc.nextLine(); // clear buffer

        if (choice == 1) {
            FileReceiver.receiveFile();
        } else {
            System.out.print("Enter full path of file to send: ");
            String path = sc.nextLine();
            FileSender.sendFile(path);
        }
    }
}
