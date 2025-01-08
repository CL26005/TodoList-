package test;
import java.util.ArrayList;
import todolist.Task;

public class AnalyticsDashboard {
    public static void main(String[] args) {
        // Step 1: Create a list of tasks
        ArrayList<Task> tasks = new ArrayList<>();

        // Add some sample tasks
        tasks.add(new Task("Math Homework", "Complete Math Exercises","2024-10-15","Homework","High","Complete"));
        tasks.add(new Task("Buy Groceries","Buy milk and bread","2024-10-16","Personal","Medium","Incomplete"));
        tasks.add(new Task("Finish Report","Complete the project report","2024-10-17","Work","High","Complete"));
        tasks.add(new Task("Science Assignment","Write a lab report","2024-10-18","Homework","Medium","Incomplete"));
        tasks.add(new Task("Exercise","Go for a run","2024-10-19","Personal","Low","Incomplete"));

        // Step 2: Call the analytics function
        showAnalytics(tasks);
    }

    public static void showAnalytics(ArrayList<Task> tasks) {
        if(tasks == null){
            System.out.println("Error: Task list is null.");
            return;
        }
        
        int totalTasks = tasks.size();
        int completed = 0;
        int pending = 0;

        // Category counts
        int homework = 0, personal = 0, work = 0;

        // Step 3: Process each task
        for (Task task : tasks) {
            if (task == null) {
                System.out.println("Warning: Found a null task in the list.");
                continue;
            }
            
            //check if the task is completed
            if ("Complete".equalsIgnoreCase(task.getStatus())){
                completed++;
            } else{
                pending++;
            }

            // Count tasks by category
            switch (task.getCategory().toLowerCase()) {
                case "homework":
                    homework++;
                    break;
                case "personal":
                    personal++;
                    break;
                case "work":
                    work++;
                    break;
                default:
                    System.out.println("Unknown category: " + task.getCategory());
                    break;
            }
        }

        // Step 4: Calculate completion rate
        double completionRate = (totalTasks == 0) ? 0 : (completed * 100.0) / totalTasks;

        // Step 5: Display the analytics
        System.out.println("=== Analytics Dashboard ===");
        System.out.println("- Total Tasks: " + totalTasks);
        System.out.println("- Completed: " + completed);
        System.out.println("- Pending: " + pending);
        System.out.printf("- Completion Rate: %.2f%%\n", completionRate);
        System.out.println("- Task Categories: Homework: " + homework + ", Personal: " + personal + ", Work: " + work);
    }
}

