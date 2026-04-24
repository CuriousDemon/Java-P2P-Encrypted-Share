package com.himangshu.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.himangshu.FileSender;
import com.himangshu.FileReceiver;

public class MainController {
    @FXML
    private Label statusLabel;

    @FXML
    protected void handleStartReceiver() {
        statusLabel.setText("Status: Listening for files...");
        // For now, we will run this in a background thread later
        // to prevent the UI from freezing.
        new Thread(() -> FileReceiver.receiveFile()).start();
    }

    @FXML
    protected void handleSendFile() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select File to Send");

        // This opens the real Windows file selector!
        java.io.File selectedFile = fileChooser.showOpenDialog(statusLabel.getScene().getWindow());

        if (selectedFile != null) {
            statusLabel.setText("Status: Sending " + selectedFile.getName() + "...");

            // Run in background so the UI stays smooth
            new Thread(() -> {
                FileSender.sendFile(selectedFile.getAbsolutePath());
                // This updates the UI text from the background thread safely
                javafx.application.Platform.runLater(() ->
                        statusLabel.setText("Status: Sent " + selectedFile.getName())
                );
            }).start();
        }
    }
}
