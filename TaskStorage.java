package todolist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import todolist.*;

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

    // Load tasks from CSV file
    public static List<String[]> loadTasksFromCSV() {
        List<String[]> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by commas and add it to the tasks list
                String[] task = line.split(",");
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }
}


