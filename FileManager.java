import java.io.*; // Import for handling input-output operations
import java.nio.file.*; // Import for file and directory paths
import java.nio.file.attribute.BasicFileAttributes; // Import for accessing file attributes
import java.util.Scanner; // Import for taking user input
import java.util.logging.*; // Import for logging

public class FileManager {

    // Logger instance for logging messages and errors
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    public static void main(String[] args) {
        setupLogger(); // Set up logging configuration

        Scanner scanner = new Scanner(System.in); // Create a scanner for user input
        System.out.println("Enter the directory path:");
        String directoryPath = scanner.nextLine(); // Read the directory path input by the user
        Path directory = Paths.get(directoryPath); // Convert the input string to a Path object

        try {
            // Check if the given path exists and is a directory
            if (Files.exists(directory) && Files.isDirectory(directory)) {
                boolean exit = false; // Flag to control the main loop
                while (!exit) {
                    displayMenu(); // Display the main menu to the user
                    int choice = scanner.nextInt(); // Get the user's choice
                    scanner.nextLine(); // Consume newline

                    // Perform action based on user's choice
                    if (choice == 1) {
                        displayDirectoryContents(directory); // Display contents of the directory
                    } else if (choice == 2) {
                        copyFile(directory, scanner); // Copy a file
                    } else if (choice == 3) {
                        moveFile(directory, scanner); // Move a file
                    } else if (choice == 4) {
                        deleteFile(directory, scanner); // Delete a file
                    } else if (choice == 5) {
                        createDirectory(directory, scanner); // Create a new directory
                    } else if (choice == 6) {
                        deleteDirectory(directory, scanner); // Delete a directory
                    } else if (choice == 7) {
                        searchFiles(directory, scanner); // Search for files
                    } else if (choice == 8) {
                        exit = true; // Exit the loop and program
                    } else {
                        System.out.println("Invalid choice. Please try again."); // Handle invalid input
                    }
                }
            } else {
                System.out.println("Directory does not exist or is not accessible."); // Handle non-existent directory
            }
        } catch (IOException e) {
            // Log and handle exceptions that occur while accessing the directory
            logger.log(Level.SEVERE, "An error occurred while accessing the directory.", e);
        }
    }

    // Method to display the menu options to the user
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

    // Method to set up the logger configuration
    private static void setupLogger() {
        try {
            // Ensure the logs directory exists
            Path logDir = Paths.get("logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir); // Create the logs directory if it does not exist
            }

            // Set up the logger with a file handler and formatter
            FileHandler fh = new FileHandler("logs/filemanager.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh); // Add the handler to the logger
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to set up logger.", e); // Log if setting up the logger fails
        }
    }

    // Method to display the contents of the given directory
    private static void displayDirectoryContents(Path directory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                // Get file attributes and display file details
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                System.out.printf("%-30s Size: %-10d Last Modified: %s%n",
                        path.getFileName(), attrs.size(), attrs.lastModifiedTime());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error displaying directory contents.", e); // Log errors while reading the directory
        }
    }

    // Method to copy a file from the directory to a specified destination
    private static void copyFile(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the file name to copy:");
        String fileName = scanner.nextLine(); // Get the file name from the user
        Path sourcePath = directory.resolve(fileName); // Resolve the file name against the directory

        if (Files.exists(sourcePath)) {
            System.out.println("Enter the destination path:");
            String destinationPath = scanner.nextLine(); // Get the destination path from the user
            Files.copy(sourcePath, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING); // Copy the file
            System.out.println("File copied successfully.");
        } else {
            System.out.println("File not found."); // Handle case where the file does not exist
        }
    }

    // Method to move a file from the directory to a specified destination
    private static void moveFile(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the file name to move:");
        String fileName = scanner.nextLine(); // Get the file name from the user
        Path sourcePath = directory.resolve(fileName); // Resolve the file name against the directory

        if (Files.exists(sourcePath)) {
            System.out.println("Enter the destination path:");
            String destinationPath = scanner.nextLine(); // Get the destination path from the user
            Files.move(sourcePath, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING); // Move the file
            System.out.println("File moved successfully.");
        } else {
            System.out.println("File not found."); // Handle case where the file does not exist
        }
    }

    // Method to delete a file from the directory
    private static void deleteFile(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the file name to delete:");
        String fileName = scanner.nextLine(); // Get the file name from the user
        Path filePath = directory.resolve(fileName); // Resolve the file name against the directory

        if (Files.exists(filePath)) {
            Files.delete(filePath); // Delete the file
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File not found."); // Handle case where the file does not exist
        }
    }

    // Method to create a new directory inside the given directory
    private static void createDirectory(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the name of the new directory:");
        String dirName = scanner.nextLine(); // Get the new directory name from the user
        Path newDirPath = directory.resolve(dirName); // Resolve the new directory name against the directory

        if (Files.exists(newDirPath)) {
            System.out.println("Directory already exists."); // Handle case where the directory already exists
        } else {
            Files.createDirectory(newDirPath); // Create the new directory
            System.out.println("Directory created successfully.");
        }
    }

    // Method to delete a directory from the given directory
    private static void deleteDirectory(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the name of the directory to delete:");
        String dirName = scanner.nextLine(); // Get the directory name from the user
        Path dirPath = directory.resolve(dirName); // Resolve the directory name against the directory

        if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
            Files.delete(dirPath); // Delete the directory
            System.out.println("Directory deleted successfully.");
        } else {
            System.out.println("Directory not found or is not empty."); // Handle case where the directory does not exist or is not empty
        }
    }

    // Method to search for files in the directory based on a search term
    private static void searchFiles(Path directory, Scanner scanner) throws IOException {
        System.out.println("Enter the search term (file name or extension):");
        String searchTerm = scanner.nextLine(); // Get the search term from the user

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*" + searchTerm + "*")) {
            for (Path path : stream) {
                System.out.println("Found: " + path.getFileName()); // Display the names of the matching files
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error searching files.", e); // Log errors while searching for files
        }
    }
}
