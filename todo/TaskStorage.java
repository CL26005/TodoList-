package todolist;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    public static void saveTaskToCSV(List<Task> tasks) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("tasks.csv"))) {
            out.println("Title,Description,Due Date,Category,Priority,Status");
            for (Task task : tasks) {
                out.println(task.getTitle() + "," + task.getDescription() + "," + task.getDueDate() + "," 
                        + task.getCategory() + "," + task.getPriority() + "," + task.getStatus());
            }
            System.out.println("Tasks saved to CSV successfully!");
        } catch (IOException e) {
            System.out.println("Error saving tasks to CSV: " + e.getMessage());
        }
    }

    // Load tasks from CSV file and return a List<Task>
    public static List<Task> loadTasksFromCSV() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] taskData = line.split(",");
                if (taskData.length >= 6) {
                    Task task = new Task(taskData);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }
}