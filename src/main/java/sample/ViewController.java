package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import jssc.SerialPortList;

import java.util.Arrays;
import java.util.List;

public class ViewController {
    @FXML
    private ComboBox<String> senderBox;

    @FXML
    private ComboBox<String> receiverBox;

    @FXML
    private TextArea senderField;

    @FXML
    private TextArea receiverField;

    @FXML
    private ComboBox<String> senderSpeedBox;

    @FXML
    private ComboBox<String> receiverSpeedBox;

    @FXML
    private Button receiverInfo;

    @FXML
    private Button senderInfo;

    @FXML
    private Button sendButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button senderSetupButton;

    @FXML
    private Button receiverSetupButton;

    private String senderName;
    private String receiverName;
    private String senderSpeed;
    private String receiverSpeed;
    private String data;

    private PortController portController;

    private boolean senderPortReady = false;
    private boolean receiverPortReady = false;

    @FXML
    private void initialize(){
        String[] portArray = SerialPortList.getPortNames();
        List<String> stringList = Arrays.asList(portArray);
        ObservableList<String> portList = FXCollections.observableList(stringList);

        this.senderBox.setItems(portList);
        this.receiverBox.setItems(portList);
        this.senderBox.setOnAction(ev -> {
            this.senderName = this.senderBox.getValue();
            this.senderInfo.setDisable(true);
            this.sendButton.setDisable(true);
            this.senderPortReady = false;
        });
        this.receiverBox.setOnAction(ev -> {
            this.receiverName = this.receiverBox.getValue();
            this.receiverInfo.setDisable(true);
            this.sendButton.setDisable(true);
            this.receiverPortReady = false;
        });

        this.receiverField.setEditable(false);
        this.senderField.setPromptText("Enter data only in binary format!");
        this.senderInfo.setDisable(true);
        this.receiverInfo.setDisable(true);
        this.sendButton.setDisable(true);

        ObservableList<String> speeds = FXCollections.observableArrayList("75", "110", "300", "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200");
        this.senderSpeedBox.setItems(speeds);
        this.receiverSpeedBox.setItems(speeds);
        this.senderSpeedBox.setOnAction(ev -> {
            this.senderSpeed = this.senderSpeedBox.getValue();
            this.senderInfo.setDisable(true);
            this.sendButton.setDisable(true);
            this.senderPortReady = false;
        });
        this.receiverSpeedBox.setOnAction(ev -> {
            this.receiverSpeed = this.receiverSpeedBox.getValue();
            this.receiverInfo.setDisable(true);
            this.sendButton.setDisable(true);
            this.receiverPortReady = false;
        });

        this.portController = new PortController(this);
    }

    private void showWarningAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClearButton(){
        this.senderField.setText("");
    }

    @FXML
    private void handleSenderSetupButton(){
        if (this.senderSpeed == null || this.senderName == null){
            showWarningAlert("Setup name and speed!");
            return;
        }
        this.portController.setupSenderComport();
        this.senderPortReady = true;
        this.senderInfo.setDisable(false);
        if (this.receiverPortReady){
            this.sendButton.setDisable(false);
        }
    }

    @FXML
    private void handleReceiverSetupButton(){
        if (this.receiverName == null || this.receiverSpeed == null){
            showWarningAlert("Setup name and speed!");
            return;
        }
        this.portController.setupReceiverComport();
        this.receiverPortReady = true;
        this.receiverInfo.setDisable(false);
        if (this.senderPortReady){
            this.sendButton.setDisable(false);
        }
    }

    @FXML
    private void handleSendButton(){

        if (this.senderName.equals(this.receiverName)){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sender and receiver com-ports cannot be equal");
            alert.showAndWait();
            return;
        }

        if (this.senderField.getText().isEmpty()){
            showWarningAlert("Enter the text");
            return;
        }

        if (!isBinaryData(this.senderField.getText())){
            showWarningAlert("Enter the data in binary format!");
            return;
        }

        this.data = this.senderField.getText();
        this.portController.sendData();
        this.receiverField.setText("Received data: " + this.portController.getReceivedData() + '\n' +
                "Unpacked data: " + this.portController.getUnpackedData());
        this.senderField.setText("Entered data: " + this.portController.getEnteredData() + '\n' +
                "Packed data: " + this.portController.getPackedData());

    }

    private boolean isBinaryData(String data){
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) != '0' && data.charAt(i) != '1'){
                return false;
            }
        }
        return true;
    }

    @FXML
    private void handleSenderPortInfoButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Com-port info");
        String parity = "";

        switch (this.portController.getSenderParity()){
            case 0: parity = "None";
                break;
            case 1: parity = "Odd";
                break;
            case 2: parity = "Even";
                break;
            case 3: parity = "Mark";
                break;
        }

        alert.setContentText("Name: " + this.senderName + "\n"
                            + "Baud rate: " + this.senderSpeed + " B/s" + "\n"
                            + "DataBits: " + this.portController.getSenderDataBits() + "\n"
                            + "StopBits: " + this.portController.getSenderStopBits() + "\n"
                            + "Parity scheme: " + parity);
        alert.showAndWait();
    }

    @FXML
    private void handleReceiverPortInfoButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Com-port info");
        String parity = "";

        switch (this.portController.getReceiverParity()){
            case 0: parity = "None";
                break;
            case 1: parity = "Odd";
                break;
            case 2: parity = "Even";
                break;
            case 3: parity = "Mark";
                break;
        }

        alert.setContentText("Name: " + this.receiverName + "\n"
                + "Baud rate: " + this.receiverSpeed + " B/s" + "\n"
                + "DataBits: " + this.portController.getReceiverDataBits() + "\n"
                + "StopBits: " + this.portController.getSenderStopBits() + "\n"
                + "Parity scheme: " + parity);
        alert.showAndWait();
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getData() {
        return data;
    }

    public String getSenderSpeed() {
        return senderSpeed;
    }

    public String getReceiverSpeed() {
        return receiverSpeed;
    }
}
