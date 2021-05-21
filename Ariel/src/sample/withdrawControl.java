package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class withdrawControl implements Initializable {

    //GUI Variables
    @FXML
    private ImageView closeButton;
    @FXML
    private Label infoLabel;
    @FXML
    private Label warnLabel;
    @FXML
    private TextField amountTxt;

    //variables got from main screen
    private Double balance;
    private String accNumber;
    HashMap<String, Account> accs = new HashMap<>();
    LinkedList<String> transactions = new LinkedList<>();
    DecimalFormat df = new DecimalFormat("###.#");

    //withdraw button method
    public void withdrawButtonClick(){

        if(amountTxt.getText().matches("[0-9]+")){
            double amount = Double.parseDouble(amountTxt.getText());
            if((amount > balance) && (amount > 2500.0)){
                warnLabel.setTextFill(Color.color(1, 0, 0));
                warnLabel.setText("You cannot withdraw more money than your balance.");
                amountTxt.setText("");
            }else if(amount > 2500.0){
                warnLabel.setTextFill(Color.color(1, 0, 0));
                warnLabel.setText("You cannot withdraw more than 2500  $ at once.");
                amountTxt.setText("");
            }else{

                //withdraw method in account object with which variables got from main screen
                accs.get(accNumber).withdraw(amount);
                balance  -= amount;

                //add new transaction with which same linked list in main screen
                Transactions ts = new Transactions("Withdraw", accNumber,accs.get(accNumber).getName(), (amount + "(amount)"));
                transactions.add(ts.toString());
                //update label
                infoLabel.setText(amount + "  $  has been withdrawn.\nAccount number   :    " + accNumber +".\nNew balance   :   $" + df.format(balance) );
                warnLabel.setTextFill(Color.color(0, 1, 0));
                warnLabel.setText("Withdraw completed succesfully.");
                amountTxt.setText("");
                accs.get(accNumber).setBalance(this.balance);
            }

        }else{
            warnLabel.setTextFill(Color.color(1, 0, 0));
            warnLabel.setText("You must enter just numbers.");
            amountTxt.setText("");
        }
    }

    //close button method
    public void closeThisGoMain(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //setters and getters
    public void setInfoLabel(String string) {
        infoLabel.setText(string);
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public void setAccs(HashMap<String, Account> accs) {
        this.accs = accs;
    }

    public void setTransactions(LinkedList<String> transactions) {
        this.transactions = transactions;
    }

    //show image
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File closeBtnFile = new File("images/closebutton2.png");
        Image closeImage = new Image(closeBtnFile.toURI().toString());
        closeButton.setImage(closeImage);

    }
}
