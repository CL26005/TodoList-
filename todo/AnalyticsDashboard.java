package todolist;

import java.util.List;

public class AnalyticsDashboard {

    public static void main(String[] args) {
        // Step 1: Load tasks from CSV
        List<Task> tasks = TaskStorage.loadTasksFromCSV();

        // Step 2: Call the analytics function
        showAnalytics(tasks);
    }

    public static void showAnalytics(List<Task> tasks) {
        int totalTasks = tasks.size();
        int completed = 0;
        int pending = 0;

        // Category counts
        int homework = 0, personal = 0, work = 0;

        // Step 3: Process each task
        for (Task task : tasks) {
            if (task.getStatus().equalsIgnoreCase("Complete")) {
                completed++;
            } else {
                pending++;
            }

            // Count tasks by category (case-insensitive comparison)
            String category = task.getCategory();
            if (category.equalsIgnoreCase("Homework")) {
                homework++;
            } else if (category.equalsIgnoreCase("Personal")) {
                personal++;
            } else if (category.equalsIgnoreCase("Work")) {
                work++;
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