package todolist;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Mail {

    public static void main(String[] args) {
        // Load tasks from CSV
        List<Task> tasks = TaskStorage.loadTasksFromCSV();

        // Check for tasks due within 24 hours
        boolean reminderSent = false;
        for (Task task : tasks) {
            LocalDate dueDate = LocalDate.parse(task.getDueDate());
            LocalDate today = LocalDate.now();
            long daysUntilDue = ChronoUnit.DAYS.between(today, dueDate);

            if (daysUntilDue <= 1 && !task.getStatus().equalsIgnoreCase("Complete")) {
                // Prompt user for email address
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter your email address to receive a reminder: ");
                String toEmail = scanner.nextLine();

                // Send email reminder
                sendEmailReminder(toEmail, task);
                System.out.println("Email reminder sent successfully to " + toEmail);

                // Set flag to indicate a reminder was sent
                reminderSent = true;

                // Exit the program after sending the email
                System.exit(0);
            }
        }

        // If no tasks are due within 24 hours, exit the program
        if (!reminderSent) {
            System.out.println("No tasks due within 24 hours.");
            System.exit(0);
        }
    }

    public static void sendEmailReminder(String toEmail, Task task) {
        final String fromEmail = "your_email@gmail.com";  
        final String password = "your_password";         

        // SMTP server settings
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");        // SMTP Host
        props.put("mail.smtp.port", "587");                  // TLS Port
        props.put("mail.smtp.auth", "true");                 // Enable authentication
        props.put("mail.smtp.starttls.enable", "true");      // Enable STARTTLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");     // Ensure TLSv1.2 is used

        // Get the Session object
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject("🔔 Task Reminder: " + task.getTitle());

            // Set the email body with HTML formatting
            String emailBody = "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h2 style='color: #4A90E2;'>Task Reminder</h2>"
                    + "<p>Dear User,</p>"
                    + "<p>This is a reminder that your task <strong>\"" + task.getTitle() + "\"</strong> is due on <strong>" + task.getDueDate() + "</strong>.</p>"
                    + "<p>Here are the details of your task:</p>"
                    + "<ul>"
                    + "<li><strong>Title:</strong> " + task.getTitle() + "</li>"
                    + "<li><strong>Description:</strong> " + task.getDescription() + "</li>"
                    + "<li><strong>Due Date:</strong> " + task.getDueDate() + "</li>"
                    + "<li><strong>Category:</strong> " + task.getCategory() + "</li>"
                    + "<li><strong>Priority:</strong> " + task.getPriority() + "</li>"
                    + "</ul>"
                    + "<p>Please ensure that you complete it on time. If you've already completed this task, you can mark it as complete in your task manager.</p>"
                    + "<p>Best regards,<br>"
                    + "SOUT Team</p>"
                    + "<p style='font-size: 12px; color: #777;'>This is an automated email. Please do not reply.</p>"
                    + "</body>"
                    + "</html>";

            // Set the email content as HTML
            message.setContent(emailBody, "text/html");

            // Send the email
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
