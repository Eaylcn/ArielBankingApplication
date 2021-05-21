package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class moneyBoxInfosController implements Initializable {

    //GUI variables
    @FXML
    private ImageView closeButton;
    @FXML
    private ImageView ccImage;
    @FXML
    private Label boxIdLabel;
    @FXML
    private Label moneyLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField pinTxt;

    //variables got from main screen
    private int pin;
    private String accNumber;
    HashMap<String, Account> accs = new HashMap<>();
    LinkedList<String> transactions = new LinkedList<>();

    //change pin button method
    public void changePIN(){
        if((pinTxt.getText().length() == 4) && (pinTxt.getText().matches("[0-9]+"))){
            int newPin = Integer.parseInt(pinTxt.getText());
            pin = newPin;

            //set new password from method in account object with which variables got from main screen
            accs.get(accNumber).moneyBoxInfos(newPin,accNumber);

            //add new transaction with which same linked list in main screen
            Transactions ts = new Transactions("MoneyBoxPinChange", accNumber,accs.get(accNumber).getName(), (newPin + "(new pin)"));
            transactions.add(ts.toString());

            //update label
            infoLabel.setTextFill(Color.color(0, 1, 0));
            infoLabel.setText("New Money Box PIN : " + newPin);
            pinTxt.setText("");
            accs.get(accNumber).setSafetyDepositBoxKey(pin);
        }else{
            infoLabel.setTextFill(Color.color(1, 0, 0));
            infoLabel.setText("Pin must consist of 4 NUMBERS.");
            pinTxt.setText(String.valueOf(this.pin));
        }
    }

    //close button method
    public void closeThisGoMain() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //Setters and Getters

    public void setBoxIdLabel(String boxIdLabel) {
        this.boxIdLabel.setText(boxIdLabel);
    }

    public void setMoneyLabel(String moneyLabel) {
        this.moneyLabel.setText(moneyLabel);
    }

    public void setPinTxt(int pinTxt) {
        this.pinTxt.setText(String.valueOf(pinTxt));
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setAccNumber(String accnum) {
        this.accNumber = accnum;
    }

    public void setAccs(HashMap<String, Account> accs) {
        this.accs = accs;
    }

    public void setTransactions(LinkedList<String> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File closeBtnFile = new File("images/closebutton2.png");
        Image closeImage = new Image(closeBtnFile.toURI().toString());
        closeButton.setImage(closeImage);
        File ccImageFile = new File("images/moneybox.png");
        Image ccImg = new Image(ccImageFile.toURI().toString());
        ccImage.setImage(ccImg);
    }

}
