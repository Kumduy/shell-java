package src;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String currentDirectory = System.getProperty("user.dir"); // Thư mục hiện tại

        System.out.println("Nhập gì đó đi");

        while (true) {
            System.out.print("MyShell> ");
            String input = scanner.nextLine().trim();
            String[] commands = input.split("\\s+");

            if (commands.length == 0 || commands[0].isEmpty()) {
                continue;
            }

            String command = commands[0];
            switch (command) {
                case "exit":
                    System.out.println("Goodbye!");
                    return;

                case "cd":
                    if (commands.length < 2) {
                        System.out.println("Usage: cd <directory>");
                    } else {
                        changeDirectory(commands[1], currentDirectory);
                        currentDirectory = new File(currentDirectory, commands[1]).getAbsolutePath();
                    }
                    break;

                case "ls":
                    listDirectory(currentDirectory);
                    break;

                case "echo":
                    echo(String.join(" ", commands));
                    break;

                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        }
    }

    /**
     * Thao tác lệnh cd
     */
    private static void changeDirectory(String dir, String currentDir) {
        File newDir = new File(currentDir, dir);
        if (newDir.exists() && newDir.isDirectory()) {
            System.out.println("Changed directory to: " + newDir.getAbsolutePath());
        } else {
            System.out.println("Directory not found: " + dir);
        }
    }

    /**
     * Duyệt qua các tệp và thư mục trong thư mục hiện tại
     */
    private static void listDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            String[] files = dir.list();
            if (files != null) {
                System.out.println("Files and directories:");
                for (String file : files) {
                    System.out.println(file);
                }
            } else {
                System.out.println("No files found.");
            }
        } else {
            System.out.println("Error accessing directory.");
        }
    }

    /**
     * Thực hiện lệnh echo
     */
    private static void echo(String message) {
        System.out.println(message);
    }
}
