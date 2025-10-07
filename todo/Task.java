package todolist;
public class Task {
    private String id;
    private String title;
    private String description;
    private String dueDate;
    private String category;
    private String priority;
    private String status;
    private String[] dependencies;

    public Task(String[] details) {
        if (details.length >= 6) {
            this.title = details[0];
            this.description = details[1];
            this.dueDate = details[2];
            this.category = details[3];
            this.priority = details[4];
            this.status = details[5];
        }
    }

    public Task(String title, String description, String dueDate, String category, String priority, String status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public String toString() {
        return title + " [" + status + "] - Due: " + dueDate + " - " + category + " - " + priority;
    }
}
