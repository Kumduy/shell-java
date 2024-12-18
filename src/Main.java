package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main  extends JFrame{
    private JTextArea displayArea;
    private JTextField inputField;
    private String currentDirectory;

    public Main() {
        setTitle("My Black Terminal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        currentDirectory =  "C:";

        // Bố cục chính
        setLayout(new BorderLayout());

        // Khu vực hiển thị lệnh
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        displayArea.setBackground(Color.BLACK);
        displayArea.setForeground(Color.GREEN);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Khu vực nhập lệnh
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);

        // Sự kiện khi nhấn Enter
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText().trim();
                displayArea.append("Duy: "+currentDirectory+"> " + command + "\n");
                executeCommand(command);
                inputField.setText("");
            }
        });

        // Thêm các thành phần vào giao diện
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }

    /**
     * Xử lý lệnh nhập vào
     */
    private void executeCommand(String command) {
        if (command.isEmpty()) {
            return;
        }

        String[] commands = command.split("\\s+");
        String mainCommand = commands[0];

        switch (mainCommand) {
            case "exit":
                displayArea.append("Tạm biệt!\n");
                System.exit(0);
                break;

            case "ls":
                listDirectory();
                break;

            case "cd":
                if (commands.length < 2) {
                    displayArea.append("Địt mẹ mày phải nhập tên file mới được chứ: cd <directory>\n");
                } else {
                    changeDirectory(command.substring(3));
                }
                break;

            case "echo":
                String message = command.substring(5); // Lấy chuỗi sau "echo "
                displayArea.append(message + "\n");
                break;

            case "run":
                if (commands.length < 2) {
                    displayArea.append("Usage: run <filename>\n");
                } else {
                    runFile(commands[1]);
                }
                break;

            default:
                displayArea.append("Bố đéo có lệnh này: " + mainCommand + "\n");
                break;
        }
    }

    /**
     * Xử lý lệnh cd
     */
    private void changeDirectory(String dir) {
        if (dir.equals("..")) {
            File parent = new File(currentDirectory).getParentFile();
            if (parent != null) {
                currentDirectory = parent.getAbsolutePath();
            }
        } else {
            File newDir = new File(currentDirectory, dir);
            if (newDir.exists() && newDir.isDirectory()) {
                currentDirectory = newDir.getAbsolutePath();
            } else {
                displayArea.append("Đéo tìm thấy thư mục này: " + dir + "\n");
                return;
            }
        }
        displayArea.append("Đã chuyển đến thư mục: " + currentDirectory + "\n");
    }

    /**
     * Xử lý lệnh ls
     */
    private void listDirectory() {
        File dir = new File(currentDirectory);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                displayArea.append(file.getName() + (file.isDirectory() ? "/" : "") + "\n");
            }
        } else {
            displayArea.append("Lỗi rồi thồn lằng.\n");
        }
    }

    /**
     * Xử lý lệnh run
     */
    private void runFile(String fileName) {
        File fileToRun = new File(currentDirectory, fileName);
        if (fileToRun.exists() && fileToRun.isFile()) {
            try {
                // Sử dụng ProcessBuilder để chạy tệp
                ProcessBuilder builder = new ProcessBuilder(fileToRun.getAbsolutePath());
                builder.directory(new File(currentDirectory)); // Đặt thư mục làm việc
                Process process = builder.start();

                // Đọc đầu ra của chương trình
                Scanner processOutput = new Scanner(process.getInputStream());
                while (processOutput.hasNextLine()) {
                    displayArea.append(processOutput.nextLine() + "\n");
                }
                processOutput.close();

                // Đọc đầu ra lỗi (nếu có)
                Scanner errorOutput = new Scanner(process.getErrorStream());
                while (errorOutput.hasNextLine()) {
                    displayArea.append("Error: " + errorOutput.nextLine() + "\n");
                }
                errorOutput.close();

            } catch (IOException e) {
                displayArea.append("Không chạy được file: " + e.getMessage() + "\n");
            }
        } else {
            displayArea.append("Không tìm thấy filr: " + fileName + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main terminal = new Main();
            terminal.setVisible(true);
        });
    }
}
