package test;

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

import javax.mail.*;
import javax.mail.internet.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class GUI extends Application {

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private String userEmail = "kwekcheeling@gmail.com"; 

    @Override
    public void start(Stage primaryStage) {
        // Task list view
        ListView<Task> taskListView = new ListView<>(taskList);
        taskListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + (item.isComplete() ? " (Complete)" : "") +
                            " - Due: " + item.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
        });

        // TextField for new task input
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a new task");

        // Date and Time input HBox
        HBox dateTimeBox = new HBox(10);
        dateTimeBox.setAlignment(Pos.CENTER);
        
        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Select due date");
        
        TextField timeInput = new TextField();
        timeInput.setPromptText("Enter time (HH:mm)");
        timeInput.setPrefWidth(100);
        
        dateTimeBox.getChildren().addAll(dueDatePicker, timeInput);

        // Add Task and Sort buttons HBox
        HBox addSortBox = new HBox(10);
        addSortBox.setAlignment(Pos.CENTER);
        
        Button addButton = new Button("Add Task");
        Button sortAscButton = new Button("Sort Ascending");
        Button sortDescButton = new Button("Sort Descending");
        
        addSortBox.getChildren().addAll(addButton, sortAscButton, sortDescButton);

        // Complete and Delete buttons HBox
        HBox completeDeleteBox = new HBox(10);
        completeDeleteBox.setAlignment(Pos.CENTER);
        
        Button markCompleteButton = new Button("Mark as Complete");
        Button deleteButton = new Button("Delete Task");
        
        completeDeleteBox.getChildren().addAll(markCompleteButton, deleteButton);

        // Email input and send reminder
        TextField emailInput = new TextField();
        emailInput.setPromptText("Enter your email address");
        Button sendEmailButton = new Button("Send Email Reminders");

        // Button functionalities remain the same
        addButton.setOnAction(event -> {
            String taskName = taskInput.getText();
            if (!taskName.isEmpty() && dueDatePicker.getValue() != null && !timeInput.getText().isEmpty()) {
                try {
                    LocalDateTime dueDate = LocalDateTime.parse(dueDatePicker.getValue() + "T" + timeInput.getText());
                    taskList.add(new Task(taskName, false, dueDate));
                    taskInput.clear();
                    dueDatePicker.setValue(null);
                    timeInput.clear();
                } catch (Exception e) {
                    showAlert("Error", "Invalid date or time format.");
                }
            } else {
                showAlert("Error", "Please fill in all fields.");
            }
        });

        deleteButton.setOnAction(event -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                taskList.remove(selectedTask);
            }
        });

        sortAscButton.setOnAction(event -> 
            taskList.sort((task1, task2) -> task1.getName().compareToIgnoreCase(task2.getName())));

        sortDescButton.setOnAction(event -> 
            taskList.sort((task1, task2) -> task2.getName().compareToIgnoreCase(task1.getName())));

        markCompleteButton.setOnAction(event -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                selectedTask.setComplete(true);
                taskListView.refresh();
            }
        });

        sendEmailButton.setOnAction(event -> {
            String emailAddress = emailInput.getText();
            if (emailAddress.isEmpty() || !emailAddress.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                showAlert("Error", "Please enter a valid email address.");
            } else {
                try {
                    sendEmailReminders(taskList, emailAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to send email: " + e.getMessage());
                }
            }
        });

        // Main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                taskInput,
                dateTimeBox,
                addSortBox,
                taskListView,
                completeDeleteBox,
                emailInput,
                sendEmailButton
        );

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setTitle("To Do List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Email sending method remains the same
    private void sendEmailReminders(ObservableList<Task> tasks, String emailAddress) throws MessagingException {
        StringBuilder emailBody = new StringBuilder("<h1>Upcoming Tasks</h1><ul>");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);

        for (Task task : tasks) {
            if (!task.isComplete() && task.getDueDate().isEqual(now) || task.getDueDate().isAfter(now) && task.getDueDate().isBefore(next24Hours)) {
                emailBody.append("<li>")
                        .append(task.getName())
                        .append(" - Due: ")
                        .append(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .append("</li>");
            }
        }

        emailBody.append("</ul>");
        if (emailBody.toString().equals("<h1>Upcoming Tasks</h1><ul></ul>")) {
            showAlert("No Reminders", "No tasks are due within 24 hours.");
            return;
        }

        String fromUser = "kwekcheeling@gmail.com"; 
        String fromUserPassword = "cblhjkvpbjzvpgib";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUser, fromUserPassword);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress)); // Use the user's email
        mimeMessage.setSubject("Upcoming Tasks Reminder");
        mimeMessage.setContent(emailBody.toString(), "text/html");

        Transport.send(mimeMessage);
        showAlert("Success", "Reminder email sent successfully!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Task class remains the same
    public static class Task {
        private String name;
        private boolean complete;
        private LocalDateTime dueDate;

        public Task(String name, boolean complete, LocalDateTime dueDate) {
            this.name = name;
            this.complete = complete;
            this.dueDate = dueDate;
        }

        public String getName() {
            return name;
        }

        public boolean isComplete() {
            return complete;
        }

        public void setComplete(boolean complete) {
            this.complete = complete;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
