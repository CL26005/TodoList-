
import java.util.ArrayList;

public class AnalyticsDashboard {
    public static void main(String[] args) {
        // Step 1: Create a list of tasks
        ArrayList<Task> tasks = new ArrayList<>();

        // Add some sample tasks
        tasks.add(new Task("Math Homework", true, "Homework"));
        tasks.add(new Task("Buy Groceries", false, "Personal"));
        tasks.add(new Task("Finish Report", true, "Work"));
        tasks.add(new Task("Science Assignment", false, "Homework"));
        tasks.add(new Task("Exercise", false, "Personal"));

        // Step 2: Call the analytics function
        showAnalytics(tasks);
    }

    public static void showAnalytics(ArrayList<Task> tasks) {
        int totalTasks = tasks.size();
        int completed = 0;
        int pending = 0;

        // Category counts
        int homework = 0, personal = 0, work = 0;

        // Step 3: Process each task
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed++;
            } else {
                pending++;
            }

            // Count tasks by category
            switch (task.getCategory()) {
                case "Homework":
                    homework++;
                    break;
                case "Personal":
                    personal++;
                    break;
                case "Work":
                    work++;
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

