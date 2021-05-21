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

public class accountInfosController implements Initializable {

    //GUI variables
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label idnumLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private TextField pwTxt;
    @FXML
    private ImageView closeButton;

    //variables got from main screen
    private String accNumber;
    HashMap<String, Account> accs = new HashMap<>();
    LinkedList<String> transactions = new LinkedList<>();

    //change pin button method
    public void changePIN(){
        if(pwTxt.getText().length() >= 6){
            String password = pwTxt.getText();

            //set new password from method in account object with which variables got from main screen
            accs.get(accNumber).setPassword(password);

            //add new transaction with which same linked list in main screen
            Transactions ts = new Transactions("Account_Password_Change", accNumber,accs.get(accNumber).getName(), (password + "(password)"));
            transactions.add(ts.toString());

            //update label
            infoLabel.setTextFill(Color.color(0,1,0));
            setInfoLabel("Your password has just changed.");
            pwTxt.setText(password);
        }else{
            infoLabel.setTextFill(Color.color(1,0,0));
            setInfoLabel("Your password must be at least 6 characters.");
            pwTxt.setText("password");
        }
    }

    //back button method
    public void closeThisGoMain() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //Setters and Getters


    public void setNameLabel(String string) {
        this.nameLabel.setText(string);
    }

    public void setEmailLabel(String string) {
        this.emailLabel.setText(string);
    }

    public void setIdnumLabel(String string) {
        this.idnumLabel.setText(string);
    }

    public void setDateLabel(String string) {
        this.dateLabel.setText(string);
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public void setInfoLabel(String infoLabel) {
        this.infoLabel.setText(infoLabel);
    }

    public TextField getPwTxt() {
        return pwTxt;
    }

    public void setAccs(HashMap<String, Account> accs) {
        this.accs = accs;
    }

    public void setTransactions(LinkedList<String> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File closeBtnFile = new File("images/closebutton.png");
        Image closeImage = new Image(closeBtnFile.toURI().toString());
        closeButton.setImage(closeImage);
    }


}
