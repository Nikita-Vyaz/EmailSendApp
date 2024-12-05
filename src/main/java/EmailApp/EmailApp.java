package EmailApp;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailApp extends JFrame {
    private static final Logger logger = LogManager.getLogger(EmailApp.class);

    private JTextField emailFileField;
    private JTextField messageFileField;
    private JButton sendButton;

    private static final String SECRET_KEY_FILE = "secret.key";
    private static final String CREDENTIALS_FILE = "C:\\Users\\dreml\\IdeaProjects\\Email\\credentials.txt";

    public EmailApp() {
        setTitle("Email Sender");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        initUI();
        initAction();
    }

    private void initUI() {
        JLabel emailFileLabel = new JLabel("Please enter the directory containing the file with the list of email addresses and names:");
        emailFileLabel.setBounds(20, 20, 800, 30);
        add(emailFileLabel);

        emailFileField = createTextField(20, 50, "Enter the file directory");

        JLabel messageFileLabel = new JLabel("Please enter the directory containing the text for the mailing:");
        messageFileLabel.setBounds(20, 90, 800, 30);
        add(messageFileLabel);

        messageFileField = createTextField(20, 120, "Enter the file directory");

        JLabel warningLabel = new JLabel("The email list file should look like this: Name,example@gmail.com");
        warningLabel.setBounds(20, 160, 800, 30);
        add(warningLabel);

        sendButton = new JButton("Start Sending");
        sendButton.setBounds(20, 200, 350, 30);
        add(sendButton);
    }

    private JTextField createTextField(int x, int y, String toolTip) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 350, 30);
        textField.setToolTipText(toolTip);
        add(textField);
        return textField;
    }

    private void initAction() {
        try {
            String[] credentials = loadCredentials(CREDENTIALS_FILE);
            String userEmail = credentials[0];
            String userPassword = decryptPassword(credentials[1]);

            sendButton.addActionListener(e -> sendEmails(userEmail, userPassword));
        } catch (Exception ex) {
            logger.error("Initialization error: ", ex);
            JOptionPane.showMessageDialog(this, "Initialization error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendEmails(String userEmail, String userPassword) {
        StringBuilder errorMessage = new StringBuilder();
        int successCount = 0;

        try {
            String emailFilePath = emailFileField.getText();
            String messageFilePath = messageFileField.getText();

            List<String[]> emailList = loadEmailList(emailFilePath);
            String messageBody = loadMessageBody(messageFilePath);

            SMTPEmailSender emailSender = new SMTPEmailSender("smtp.gmail.com", userEmail, userPassword);
            for (String[] recipient : emailList) {
                try {
                    sendMail(emailSender, recipient, messageBody);
                    successCount++;
                } catch (Exception e) {
                    logger.error("Error sending email to " + recipient[1] + ": " + e.getMessage());
                    errorMessage.append("Error sending to " + recipient[1] + ": " + e.getMessage() + "\n");
                }
            }

            // Summary message about sending results
            if (successCount > 0) {
                String message = successCount + " emails successfully sent!";
                if (errorMessage.length() > 0) {
                    message += "\nHowever, the following errors occurred:\n" + errorMessage.toString();
                }
                JOptionPane.showMessageDialog(this, message, "Send Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No emails were sent.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            logger.error("Error sending emails: ", ex);
            JOptionPane.showMessageDialog(this, "Error sending emails: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendMail(SMTPEmailSender emailSender, String[] recipient, String messageBody) throws Exception {
        String recipientName = recipient[0];
        String recipientEmail = recipient[1];
        emailSender.sendEmail(recipientEmail, recipientName, messageBody);
    }

    private List<String[]> loadEmailList(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines()
                    .map(line -> line.split(","))
                    .map(arr -> new String[]{arr[0].trim(), arr[1].trim()})
                    .toList();
        } catch (IOException e) {
            logger.error("Error reading the email list file: ", e);
            return List.of();
        }
    }

    private String loadMessageBody(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("Error reading the message file: ", e);
            return "";
        }
    }

    private String[] loadCredentials(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            logger.error("Error reading the credentials file: ", e);
            return new String[]{"", ""};
        }
    }

    private String decryptPassword(String encryptedPassword) throws Exception {
        SecretKey key = loadSecretKey();
        return Encryptor.decrypt(encryptedPassword, key);
    }

    private SecretKey loadSecretKey() throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SECRET_KEY_FILE))) {
            return (SecretKey) ois.readObject();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmailApp app = new EmailApp();
            app.setVisible(true);
        });
    }
}
