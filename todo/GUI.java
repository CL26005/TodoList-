package todolist;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class GUI extends Application {

    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Load tasks from CSV file
        loadTasksFromCSV();

        // Header
        Label header = new Label("To-Do List");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Task list view
        ListView<Task> taskListView = new ListView<>(taskList);
        taskListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a checkbox for marking tasks as complete
                    CheckBox checkBox = new CheckBox(item.getTitle() + " - Due: " + item.getDueDate() +
                            " - Priority: " + item.getPriority() + " - Status: " + item.getStatus());
                    checkBox.setSelected(item.getStatus().equalsIgnoreCase("Complete"));
                    checkBox.setOnAction(event -> {
                        // Update the task status based on the checkbox state
                        item.setStatus(checkBox.isSelected() ? "Complete" : "Incomplete");
                        saveTasksToCSV(); // Save changes to CSV
                        taskListView.refresh(); // Refresh the ListView to reflect the changes
                    });

                    // Create a delete button for each task
                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> {
                        taskList.remove(item); // Remove the task from the list
                        saveTasksToCSV(); // Save changes to CSV
                        showAlert("Success", "Task deleted successfully!");
                    });

                    // Create an edit button for each task
                    Button editButton = new Button("Edit");
                    editButton.setOnAction(event -> {
                        editTask(item);
                        taskListView.refresh(); // Refresh the ListView to reflect the changes
                    });

                    // Add the checkbox, delete button, and edit button to an HBox
                    HBox taskBox = new HBox(10, checkBox, deleteButton, editButton);
                    taskBox.setAlignment(Pos.CENTER_LEFT);

                    // Set the HBox as the graphic for the list cell
                    setGraphic(taskBox);
                }
            }
        });

        // Add Task Section
        Label addTaskLabel = new Label("Add New Task:");
        TextField taskTitleInput = new TextField();
        taskTitleInput.setPromptText("Task Title");
        TextField taskDescriptionInput = new TextField();
        taskDescriptionInput.setPromptText("Task Description");
        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");
        TextField taskCategoryInput = new TextField();
        taskCategoryInput.setPromptText("Category (e.g., Work, Personal, Homework)");
        TextField taskPriorityInput = new TextField();
        taskPriorityInput.setPromptText("Priority (Low, Medium, High)");

        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(event -> {
            String title = taskTitleInput.getText();
            String description = taskDescriptionInput.getText();
            LocalDate dueDate = dueDatePicker.getValue();
            String category = taskCategoryInput.getText();
            String priority = taskPriorityInput.getText();

            if (title.isEmpty() || description.isEmpty() || dueDate == null || category.isEmpty() || priority.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
            } else {
                Task newTask = new Task(title, description, dueDate.toString(), category, priority, "Incomplete");
                taskList.add(newTask);
                saveTasksToCSV(); // Save new task to CSV
                taskTitleInput.clear();
                taskDescriptionInput.clear();
                dueDatePicker.setValue(null);
                taskCategoryInput.clear();
                taskPriorityInput.clear();
            }
        });

        // Sort Buttons
        Button sortAscendingButton = new Button("Sort Ascending");
        Button sortDescendingButton = new Button("Sort Descending");

        sortAscendingButton.setOnAction(event -> {
            taskList.sort((task1, task2) -> task1.getTitle().compareToIgnoreCase(task2.getTitle()));
        });

        sortDescendingButton.setOnAction(event -> {
            taskList.sort((task1, task2) -> task2.getTitle().compareToIgnoreCase(task1.getTitle()));
        });

        // Layouts
        VBox addTaskBox = new VBox(10, addTaskLabel, taskTitleInput, taskDescriptionInput, dueDatePicker, taskCategoryInput, taskPriorityInput, addTaskButton);
        HBox buttonBox = new HBox(10, sortAscendingButton, sortDescendingButton);

        VBox layout = new VBox(10, header, addTaskBox, taskListView, buttonBox);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Load tasks from CSV file
    private void loadTasksFromCSV() {
        List<Task> tasks = TaskStorage.loadTasksFromCSV();
        taskList.addAll(tasks);
    }

    // Save tasks to CSV file
    private void saveTasksToCSV() {
        TaskStorage.saveTaskToCSV(taskList);
    }

    // Show alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Edit Task
    private void editTask(Task task) {
        // Create a dialog to edit the task
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");
        dialog.setHeaderText("Edit the task details:");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the input fields
        TextField titleField = new TextField(task.getTitle());
        TextField descriptionField = new TextField(task.getDescription());
        DatePicker dueDatePicker = new DatePicker(LocalDate.parse(task.getDueDate()));
        TextField categoryField = new TextField(task.getCategory());
        TextField priorityField = new TextField(task.getPriority());

        // Add the fields to the dialog
        dialog.getDialogPane().setContent(new VBox(10,
                new Label("Title:"), titleField,
                new Label("Description:"), descriptionField,
                new Label("Due Date:"), dueDatePicker,
                new Label("Category:"), categoryField,
                new Label("Priority:"), priorityField));

        // Convert the result to a Task object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                task.setTitle(titleField.getText());
                task.setDescription(descriptionField.getText());
                task.setDueDate(dueDatePicker.getValue().toString());
                task.setCategory(categoryField.getText());
                task.setPriority(priorityField.getText());
                saveTasksToCSV(); // Save changes to CSV
                return task;
            }
            return null;
        });

        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}