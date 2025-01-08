package todolist;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
import javafx.collections.ObservableList;
import java.util.HashSet;

public class SimpleTaskManager {
    
    static ArrayList<String[]> tasks = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
//    private static int counter=0;
//    private static String id;
    
    //shared task list from frontend
    private ObservableList<GUI.Task> sharedTaskList;
    
    //setter for the shared task list
    public void setSharedTaskList (ObservableList<GUI.Task> sharedTaskList){
        this.sharedTaskList = sharedTaskList;
    }

    //start method to run the task manager

    public static void main(String[] args) {
        loadTasksFromCSV();
        while (true) {
            System.out.println("\n=== Task Manager ===");
            System.out.println("1. Add Task");
            System.out.println("2. Mark Task as Complete");
            System.out.println("3. Delete Task");
            System.out.println("4. View Tasks");
            System.out.println("5. Sort Tasks");
            System.out.println("6. Search Tasks");
            System.out.println("7. Add a Reccuring Task");
            System.out.println("8. Task Dependencies");
            System.out.println("9. Edit Task");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == 1) {
                addTask();
                saveTaskToCSV();
            } else if (choice == 2) {
                markTaskAsComplete();
                saveTaskToCSV();
            } else if (choice == 3) {
                deleteTask();
                saveTaskToCSV();
            } else if (choice == 4) {
                viewTasks();
                saveTaskToCSV();
            } else if (choice == 5) {
                sortTasks();
                saveTaskToCSV();
            } else if (choice == 6) {
                searchTasks();
                saveTaskToCSV();
            } else if (choice == 7){
                ReccuringTask();
                saveTaskToCSV();
            } else if (choice == 8) {
                taskDependencies();
                saveTaskToCSV();
            } else if (choice == 9){
                editTask();
                saveTaskToCSV();
            }else if (choice == 10) {
                System.out.println("Exiting Task Manager.");
                saveTaskToCSV();
                break;
            }else {
                System.out.println("Invalid choice! Try again.");
            }
        }
    }
    
    static void addTask() {
        System.out.println("\n=== Add a New Task ===");
        privateAddTask("None");
    }
    
    public class TaskIdManager {
        private static final String COUNTER_FILE = "counter.txt"; // File to store the last ID
        private static AtomicInteger counter = new AtomicInteger(loadCounter()); // AtomicInteger for thread-safe counting

        // Generate the next unique task ID
        public static synchronized String generateTaskId() {
            int newId = counter.incrementAndGet();
            saveCounter(newId); // Save the updated ID back to the file
            return Integer.toString(newId); // Return the new ID as a String
        }

        // Load the last ID from the file
        private static int loadCounter() {
            try (BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE))) {
                String line = reader.readLine();
                return (line != null) ? Integer.parseInt(line) : 0;
            } catch (IOException | NumberFormatException e) {
                // If file not found or invalid content, start from 0
                return 0;
            }
        }

        // Save the current ID to the file
        private static void saveCounter(int value) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
                writer.write(Integer.toString(value));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
//    static synchronized String generateTaskId() {
//        counter++;
//        return Integer.toString(counter); // Convert the counter to a String
//    }
    
    static void privateAddTask(String recurring) {
        String recurrence = recurring;
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        // user input due date
        LocalDate dueDate = LocalDate.now();
        boolean validDate = false;
        while (!validDate) {
            try {
                System.out.print("Enter due date (YYYY-MM-DD): ");
                dueDate = LocalDate.parse(scanner.nextLine()); 
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
        
        System.out.print("Enter task category (Homework, Personal, Work): ");
        String category = scanner.nextLine();

        //user input priority level
        boolean validPriorityLevel = false;
        String priorityLevel = "low";
        while (!validPriorityLevel) {
            System.out.print("Priority level (Low, Medium, High): ");
            priorityLevel = scanner.nextLine();
            priorityLevel.toLowerCase();
            if (!priorityLevel.equalsIgnoreCase("Low") && 
                !priorityLevel.equalsIgnoreCase("Medium") && 
                !priorityLevel.equalsIgnoreCase("High")) {
                    System.out.println("Invalid priority level. Please try again.");
            } else {
                validPriorityLevel = true;
            }
        }
        // TODO:
        //  generate id as the first element, cannot duplicate
        //  id = tasks length + 1
        
        //  dependency is empty at start = ""
        //                                                                                              Recurrance  id  Dependencies
        String id = TaskIdManager.generateTaskId();
        String[] task = {title, description, dueDate.toString(), category, priorityLevel, "Incomplete", recurrence, id, "|"};
        tasks.add(task);
        System.out.println("Task \"" + title + "\" added successfully!");
    }

    // TODO
    //  before marking task as done, check if its dependencies is completed.
    //  dependencies is the last element in task, seperated by character "|"
    //  dependencies = task.getLast().split("|")
    //  loop through dependencies, if all is completed, mark task as completed
    //  else show error message
    static void markTaskAsComplete() {
        viewTasks();
        System.out.println("\n=== Mark Task as Complete ===");
        System.out.print("Enter task ID to mark as complete: ");
        String taskNumber = scanner.nextLine();

        boolean found = false;
        for (String[] task : tasks) {
            if (taskNumber.equals(task[7])) {
                if (task[8].equals("|")) {
                    task[5] = "Complete";
                    System.out.println("Task \"" + task[0] + "\" marked as complete!");
                    found = true;
                } else {
                    String[] dependencies = task[8].split("\\|");
                    boolean allDependenciesComplete = true;
                    for (String dependency : dependencies) {
                        if (!dependency.isEmpty()) {
                            String[] depTask = findTaskById(dependency);
                            if (depTask != null && depTask[5].equals("Incomplete")) {
                                allDependenciesComplete = false;
                                break;
                            }
                        }
                    }
                    if (allDependenciesComplete) {
                        task[5] = "Complete";
                        System.out.println("Task \"" + task[0] + "\" marked as complete!");
                        found = true;
                    } else {
                        System.out.println("Warning: Task \"" + task[0] + "\" cannot be marked as complete because it depends on incomplete tasks.");
                    }
                }
            }
        }
        if (!found) {
            System.out.println("Invalid task number!");
        }
    
    }

    static void deleteTask() {
        viewTasks();
        System.out.println("\n=== Delete a Task ===");
        System.out.print("Enter task ID to delete: ");
        String taskNumber = scanner.nextLine();

        boolean found = false;
        for (int i=0; i<tasks.size(); i++) {
            if (taskNumber.equals(tasks.get(i)[7])) {
                System.out.println("Task \"" + tasks.get(i)[0] + "\" deleted successfully!");
                tasks.remove(i);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Invalid task number!");
        }
    }

    static void viewTasks() {
        System.out.println("\n=== View Tasks ===");
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (String[] task : tasks) {
                System.out.printf("Task ID: %-3s %-35s [ Description: %-15s Due: %-12s Category: %-10s Priority: %-7s]  (%s)\n",
                        task[7], task[0], task[1], task[2], task[3], task[4], task[5]);
                if (!task[8].equals("|")) {
                    System.out.println("   Depends on: " + task[8].replace("|", ", "));
                }
            }
        }
    }

    static void searchTasks() {
        System.out.println("\n=== Search Tasks ===");
        System.out.print("Enter a keyword to search by title or description: ");
        String seq = scanner.nextLine();

        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            String[] task = tasks.get(i); 
            if (task[0].contains(seq) || task[1].contains(seq)) {
                System.out.printf("Task ID: %-3s %-35s [ Description: %-15s Due: %-12s Category: %-10s Priority: %-7s]  (%s)\n", task[7], task[0], task[1], task[2], task[3], task[4], task[5]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks found matching the keyword.");
        }
    }
    
    static void ReccuringTask() {
        System.out.println("\n=== Add a Reccuring Task ===");
        //user input recurrence interval
        boolean validRecurrenceInterval = false;
        String recurrence = "daily";
        while (!validRecurrenceInterval) {
            System.out.print("Enter recurrence interval (daily, weekly, monthly): ");
            recurrence = scanner.nextLine();
            recurrence.toLowerCase();
            if (!recurrence.equalsIgnoreCase("daily") && 
                !recurrence.equalsIgnoreCase("weekly") && 
                !recurrence.equalsIgnoreCase("monthly")) {
                    System.out.println("Invalid recurrence interval. Please try again.");
            } else {
                validRecurrenceInterval = true;
            }
        }
        privateAddTask(recurrence);
    }
    
    static void isRecurring(int i) {
        String[] task = tasks.get(i);

        if (!task[6].equals("None")) {
            if (task[5].equals("Complete")) {
                String recurrence = task[6];
                LocalDate dueDate = LocalDate.parse(task[2]);

                String[] newTask = task.clone(); 
                switch (recurrence) {
                    case "daily":
                        newTask[2] = dueDate.plusDays(1).toString();
                        break;
                    case "weekly":
                        newTask[2] = dueDate.plusWeeks(1).toString();
                        break;
                    case "monthly":
                        newTask[2] = dueDate.plusMonths(1).toString();
                        break;
                }

            newTask[5] = "Incomplete";
            String id = TaskIdManager.generateTaskId();
            newTask[7] = id;
            tasks.add(newTask); 
            System.out.println("Recurring task added: " + newTask[0] + " (Due: " + newTask[2] + ")");
            }
        }
    }
    
    static void sortTasks() {
        System.out.println("\n===  Sort Tasks ===");
        System.out.println("Sort by:");
        System.out.println("1. Due Date (Ascending)");
        System.out.println("2. Due Date (Descending)");
        System.out.println("3. Priority (High to Low)");
        System.out.println("4. Priority (Low to High)\n");
        System.out.print("> ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                AscendingDueDateSort ();
                System.out.println("Tasks sorted by Due Date (Ascending)!");
                break;
            case 2:
                AscendingDueDateSort ();
                Collections.reverse(tasks);
                System.out.println("Tasks sorted by Due Date (Descending)!");
                break;
            case 3:
                AscendingPrioritySort ();
                Collections.reverse(tasks);
                System.out.println("Tasks sorted by Priority (High to Low)!");
                break;
            case 4:
                AscendingPrioritySort ();
                System.out.println("Tasks sorted by  Priority (Low to High)!");
                break;
            default:
                System.out.println("Invalid choice!");
        }

        viewTasks();
    }

    static void AscendingDueDateSort () {
        for (int i=0; i<tasks.size(); i++) {
            for (int j=i+1; j<tasks.size(); j++) {
                LocalDate dueDateI = LocalDate.parse(tasks.get(i)[2]);
                LocalDate dueDateJ = LocalDate.parse(tasks.get(j)[2]);
                if (dueDateI.isAfter(dueDateJ)) {
                    String[] temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j,temp);
                }    
            }
        }
    }

    static void AscendingPrioritySort () {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i+1; j < tasks.size(); j++) {
                String priorityI = tasks.get(i)[4];
                String priorityJ = tasks.get(j) [4];
                int priorityLevelI = priorityLevel(priorityI);
                int priorityLevelJ = priorityLevel(priorityJ);
                if (priorityLevelI > priorityLevelJ) {
                    String[] temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, temp);
                }

            }
        }
    }

    static int priorityLevel (String priority) {
        int level=0;
        switch (priority) {
            case "low" :
                level = 1;
                break;
            case "medium" :
                level =2 ;
                break;
            case "high" :
                level = 3;
                break;
        }
        return level;   
    }

    static void taskDependencies() {
        System.out.println("\n=== Task Dependencies ===");
        System.out.print("Enter task number that depends on another task: ");
        String dependentTaskId = scanner.nextLine();
        System.out.print("Enter the task number it depends on: ");
        String precedingTaskId = scanner.nextLine();

        String[] dependentTask = findTaskById(dependentTaskId);
        String[] precedingTask = findTaskById(precedingTaskId);

        if (dependentTask != null && precedingTask != null) {
            if (hasCycle(dependentTaskId, precedingTaskId)) {
                System.out.println("Error: Adding this dependency would create a cycle.");
            } else {
                if (dependentTask[8].equals("|")) {
                    dependentTask[8] = precedingTaskId;
                } else {
                    dependentTask[8] += "|" + precedingTaskId;
                }
                System.out.println("Task \"" + dependentTask[0] + "\" now depends on \"" + precedingTask[0] + "\".");
            }
        } else {
            System.out.println("Invalid task number!");
        }
    }

    static boolean hasCycle(String dependentTaskId, String precedingTaskId) {
        HashSet<String> visited = new HashSet<>();
        return hasCycleUtil(precedingTaskId, dependentTaskId, visited);
    }

    static boolean hasCycleUtil(String currentTaskId, String targetTaskId, HashSet<String> visited) {
        if (currentTaskId.equals(targetTaskId)) {
            return true;
        }
        if (visited.contains(currentTaskId)) {
            return false;
        }
        visited.add(currentTaskId);

        String[] currentTask = findTaskById(currentTaskId);
        if (currentTask != null && !currentTask[8].equals("|")) {
            String[] dependencies = currentTask[8].split("\\|");
            for (String dependency : dependencies) {
                if (!dependency.isEmpty() && hasCycleUtil(dependency, targetTaskId, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    static String[] findTaskById(String taskId) {
        for (String[] task : tasks) {
            if (task[7].equals(taskId)) {
                return task;
            }
        }
        return null;
    }
       
    static void editTask(){
        viewTasks();
        System.out.println("=== Edit Task ===");

        // Get the task number to edit
        boolean found = false;
        int num=0;
        while (!found) {
            System.out.print("Enter the task ID you want to edit: ");
            int taskNum = Integer.parseInt(scanner.nextLine());
            String taskNumber = String.valueOf(taskNum);
        
            for (int i=0; i<tasks.size(); i++) {
                if (taskNumber.equals(tasks.get(i)[7])) {
                    found = true;
                    num = i;
                }
            }
            if (!found) {
                System.out.println("Invalid task number! Please try again.");
            }
        }
        
    
        // Show options for editing the task
        System.out.println();
        System.out.println("What would you like to edit?");
        System.out.println("1. Title");
        System.out.println("2. Description");
        System.out.println("3. Due Date");
        System.out.println("4. Category");
        System.out.println("5. Priority");
        System.out.println("6. Set Task Dependency");
        System.out.println("7. Cancel");
        System.out.println();
        System.out.print("> ");
        int edit = scanner.nextInt();
        scanner.nextLine();  // Consume the remaining newline

        // Process the selected edit option
        switch(edit){
            case 1 -> { 
                // Edit title
                System.out.print("\nEnter the new title: ");
                String newTitle = scanner.nextLine();
                System.out.println("\nTask title \"" + tasks.get(num)[0] + "\" has been updated to \"" + newTitle + ".\"");
                tasks.get(num)[0] = newTitle;
                viewTasks();  // Display updated tasks
            }
            case 2 -> { 
                // Edit description
                System.out.print("\nEnter the new description: ");
                String newDesc = scanner.nextLine();
                System.out.println("\nTask description \"" + tasks.get(num)[1] + "\" has been updated to \"" + newDesc + ".\"");
                tasks.get(num)[1] = newDesc;
                viewTasks();  // Display updated tasks
            }
            case 3 -> {
                // Edit due date
                LocalDate newDueDate = LocalDate.now();
                boolean validDate = false;
                while (!validDate) {
                    try {
                        System.out.print("Enter due date (YYYY-MM-DD): ");
                        newDueDate = LocalDate.parse(scanner.nextLine()); 
                        System.out.println("\nTask due date \"" + tasks.get(num)[2] + "\" has been updated to \"" + newDueDate + ".\"");
                        tasks.get(num)[2] = newDueDate.toString();
                        validDate = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please try again.");
                    }
                }
                
                viewTasks();  // Display updated tasks
            }
            case 4 -> { 
                // Edit category
                System.out.print("Enter the new category (Homework, Personal, Work): ");
                String newCat = scanner.nextLine();
                System.out.println("\nTask category \"" + tasks.get(num)[3] + "\" has been updated to \"" + newCat + ".\"");
                tasks.get(num)[3] = newCat;
                viewTasks();  // Display updated tasks
            }
            case 5 -> { 
                // Edit priority
                boolean validPriorityLevel = false;
                String newPri = "low";
                while (!validPriorityLevel) {
                    System.out.print("Priority level (Low, Medium, High): ");
                    newPri = scanner.nextLine();
                    newPri.toLowerCase();
                    if (!newPri.equalsIgnoreCase("Low") && 
                        !newPri.equalsIgnoreCase("Medium") && 
                        !newPri.equalsIgnoreCase("High")) {
                            System.out.println("Invalid priority level. Please try again.");
                    } else {
                        System.out.println("\nTask priority \"" + tasks.get(num)[4] + "\" has been updated to \"" + newPri.toLowerCase() + ".\"");
                        tasks.get(num)[4] = newPri.toLowerCase();
                        validPriorityLevel = true;
                    }
                }
                viewTasks();  // Display updated tasks
            }
            case 6 -> { 
                // Set task dependency
                taskDependencies();
                viewTasks();  // Display updated tasks with dependencies
            }
            case 7 -> { 
                // Cancel and return to main menu
                System.out.println("Back to Main Menu");
                System.out.println();
            }
            default -> System.out.println("Invalid input!");  // Handle invalid input
        }  
    }

    static void loadTasksFromCSV() {
    tasks.clear();
    try (BufferedReader reader = new BufferedReader(new FileReader("tasks.csv"))) {
        String line;
        boolean isFirstLine = true;
        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            String[] taskData = line.split(",");
            if (taskData.length == 9) {
                tasks.add(taskData);
            }
        }
//        System.out.println("Tasks loaded from CSV successfully!");
    } catch (IOException e) {
        System.out.println("Error loading tasks from CSV: " + e.getMessage());
    }
}

    static void saveTaskToCSV() {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("tasks.csv"))) {
            out.println("Title,Description,Due Date,Category,Priority,IsComplete,Recurring,Id,Dependency");
            for (String[] task : tasks) {
                String taskLine = String.join(",", task);
//                System.out.println(taskLine);
                out.println(taskLine);
            }
//            System.out.println("Tasks saved to CSV successfully!");
            out.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to CSV: " + e.getMessage());
        }
    }
}
