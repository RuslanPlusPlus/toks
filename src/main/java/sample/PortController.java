package sample;

import jssc.SerialPort;
import jssc.SerialPortException;

public class PortController {
    private String senderName;
    private String receiverName;

    private int senderSpeed;
    private int receiverSpeed;
    private int senderDataBits;
    private int senderStopBits;
    private int senderParity;

    private int receiverDataBits;
    private int receiverStopBits;
    private int receiverParity;

    private String enteredData;
    private String binaryEnteredData;
    private String enteredCRCData;

    private String receivedData;
    private String decodedReceivedData;

    //private final String BITFLAG = "01111110";

    private ViewController viewController;

    public PortController(ViewController viewController){
        this.viewController = viewController;
    }

    public void setupSenderComport(){
        this.senderName = this.viewController.getSenderName();
        this.senderSpeed = Integer.parseInt(this.viewController.getSenderSpeed());
        this.senderDataBits = getSenderDataBits();
        this.senderStopBits = getSenderStopBits();
        this.senderParity = getSenderParity();

    }

    public void setupReceiverComport(){
        this.receiverName = this.viewController.getReceiverName();
        this.receiverSpeed = Integer.parseInt(this.viewController.getReceiverSpeed());
        this.receiverDataBits = getReceiverDataBits();
        this.receiverStopBits = getReceiverStopBits();
        this.receiverParity = getReceiverParity();
    }

    public void sendData(){
        SerialPort sender = new SerialPort(this.senderName);
        SerialPort receiver = new SerialPort(this.receiverName);

        try {
            sender.openPort();
            receiver.openPort();
            sender.setParams(this.senderSpeed, getSenderDataBits(), getSenderStopBits(), getSenderParity());
            receiver.setParams(this.receiverSpeed, getReceiverDataBits(), getReceiverStopBits(), getReceiverParity());

            this.enteredData = this.viewController.getData();

            this.binaryEnteredData = DataConverter.toBinaryFormat(this.enteredData);
            this.enteredCRCData = CRCCodingUtil.formDataToSend(this.binaryEnteredData);
            sender.writeString(this.enteredCRCData);

            this.receivedData = receiver.readString();

            this.decodedReceivedData = DataConverter.toNormalFormat(CRCCodingUtil.removeCRS(this.receivedData));

            sender.closePort();
            receiver.closePort();

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public String getEnteredData() {
        return enteredData;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public String getBinaryEnteredData() {
        return binaryEnteredData;
    }

    public String getDecodedReceivedData() {
        return decodedReceivedData;
    }

    public String getEnteredCRCData() { return enteredCRCData; }

    /*private String doBitStuffing(String data){
        if (!data.contains("0111111")){
            return data;
        }
        String temp = new String();
        boolean doCheck = false;
        int counter = 0;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '0' && i < data.length() - 1){
                if (data.charAt(i + 1) == '1'){
                    doCheck = true;
                }
            }

            if (doCheck && data.charAt(i) == '1'){
                counter++;
            }

            if (counter < 6){
                temp += data.charAt(i);
            }
            else{
                temp = temp + data.charAt(i) + '|' + '1' + '|';
                counter = 0;
                doCheck = false;
            }
        }
        return temp;
    }*/

    /*private String doDeStuffing(String data){
        String str = new String();
        String temp = new String();
        boolean doCheck = false;
        int counter = 0;
        str = data.replace(" ", "");
        str = str.replace("|", "");
        str = str.replace("01111110", "");

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0' && i < str.length() - 1){
                if (str.charAt(i + 1) == '1'){
                    doCheck = true;
                }
            }

            if (doCheck && str.charAt(i) == '1'){
                counter++;
            }

            if(counter < 6){
                temp += str.charAt(i);
            }
            else {
                doCheck = false;
                counter = 0;
            }
        }
        return temp;
    }*/

    /*private String packData(String data){
        return BITFLAG + ' ' + data + ' ' + BITFLAG;
    }*/

    public int getSenderDataBits(){
        return SerialPort.DATABITS_8;
    }

    public int getSenderStopBits(){
        return SerialPort.STOPBITS_1;
    }

    public int getSenderParity(){
        return SerialPort.PARITY_NONE;
    }

    public int getReceiverDataBits(){
        return SerialPort.DATABITS_8;
    }

    public int getReceiverStopBits(){
        return SerialPort.STOPBITS_1;
    }

    public int getReceiverParity(){
        return SerialPort.PARITY_NONE;
    }

}
