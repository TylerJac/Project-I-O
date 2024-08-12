import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.logging.*;

public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    public static void main(String[] args) {
        setupLogger();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the directory path:");
        String directoryPath = scanner.nextLine();
        Path directory = Paths.get(directoryPath);

        try {
            if (Files.exists(directory) && Files.isDirectory(directory)) {
                boolean exit = false;
                while (!exit) {
                    displayMenu();
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice == 1) {
                        displayDirectoryContents(directory);
                    } else if (choice == 2) {
                        copyFile(directory, scanner);
                    } else if (choice == 3) {
                        moveFile(directory, scanner);
                    } else if (choice == 4) {
                        deleteFile(directory, scanner);
                    } else if (choice == 5) {
                        createDirectory(directory, scanner);
                    } else if (choice == 6) {
                        deleteDirectory(directory, scanner);
                    } else if (choice == 7) {
                        searchFiles(directory, scanner);
                    } else if (choice == 8) {
                        exit = true;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else {
                System.out.println("Directory does not exist or is not accessible.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while accessing the directory.", e);
        }
    }

    private static void displayMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Display Directory Contents");
        System.out.println("2. Copy File");
        System.out.println("3. Move File");
        System.out.println("4. Delete File");
        System.out.println("5. Create Directory");
        System.out.println("6. Delete Directory");
        System.out.println("7. Search Files");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void setupLogger() {
        try {
            // Ensure the logs directory exists
            Path logDir = Paths.get("logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            // Set up the logger
            FileHandler fh = new FileHandler("logs/filemanager.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to set up logger.", e);
        }
    }


    private static void displayDirectoryContents(Path directory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                System.out.printf("%-30s Size: %-10d Last Modified: %s%n",
                        path.getFileName(), attrs.size(), attrs.lastModifiedTime());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error displaying directory contents.", e);
        }
    }

    private static void copyFile(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the file name to copy:");
        String fileName = scanner.nextLine();
        Path sourcePath = directory.resolve(fileName);

        if (Files.exists(sourcePath)) {
            System.out.println("Enter the destination path:");
            String destinationPath = scanner.nextLine();
            Files.copy(sourcePath, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void moveFile(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the file name to move:");
        String fileName = scanner.nextLine();
        Path sourcePath = directory.resolve(fileName);

        if (Files.exists(sourcePath)) {
            System.out.println("Enter the destination path:");
            String destinationPath = scanner.nextLine();
            Files.move(sourcePath, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void deleteFile(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the file name to delete:");
        String fileName = scanner.nextLine();
        Path filePath = directory.resolve(fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void createDirectory(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the name of the new directory:");
        String dirName = scanner.nextLine();
        Path newDirPath = directory.resolve(dirName);

        if (Files.exists(newDirPath)) {
            System.out.println("Directory already exists.");
        } else {
            Files.createDirectory(newDirPath);
            System.out.println("Directory created successfully.");
        }
    }

    private static void deleteDirectory(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the name of the directory to delete:");
        String dirName = scanner.nextLine();
        Path dirPath = directory.resolve(dirName);

        if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
            Files.delete(dirPath);
            System.out.println("Directory deleted successfully.");
        } else {
            System.out.println("Directory not found or is not empty.");
        }
    }

    private static void searchFiles(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the search term (file name or extension):");
        String searchTerm = scanner.nextLine();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*" + searchTerm + "*")) {
            for (Path path : stream) {
                System.out.println("Found: " + path.getFileName());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error searching files.", e);
        }
    }
}
