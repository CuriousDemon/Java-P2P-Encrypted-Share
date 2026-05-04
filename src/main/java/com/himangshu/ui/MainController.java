package com.himangshu.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import com.himangshu.FileSender;
import com.himangshu.FileReceiver;
import com.himangshu.DiscoveryService;

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
        // 1. Get the selected IP from the list
        String selectedIp = peerListView.getSelectionModel().getSelectedItem();

        // 2. Validation: Check if the user actually clicked an IP
        if (selectedIp == null) {
            statusLabel.setText("Status: Please select a peer from the list first!");
            return;
        }

        // 3. Open the File Chooser
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        java.io.File selectedFile = fileChooser.showOpenDialog(statusLabel.getScene().getWindow());

        if (selectedFile != null) {
            statusLabel.setText("Status: Sending to " + selectedIp + "...");

            // 4. Run the sender logic in a background thread
            new Thread(() -> {
                // Pass BOTH the file path and the selected IP
                FileSender.sendFile(selectedFile.getAbsolutePath(), selectedIp);

                javafx.application.Platform.runLater(() ->
                        statusLabel.setText("Status: Successfully sent to " + selectedIp)
                );
            }).start();
        }
    }

    @FXML
    private ListView <String> peerListView;

    // Add this to your Controller to link the Discovery Service to the UI
    public void initialize() {
        // Check for new peers every 2 seconds
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        peerListView.getItems().clear();
                        peerListView.getItems().addAll(DiscoveryService.getDiscoveredPeers());
                    });
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }).start();
    }
}
