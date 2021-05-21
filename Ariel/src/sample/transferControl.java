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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class transferControl implements Initializable {

    //GUI variables
    @FXML
    private ImageView closeButton;
    @FXML
    private Label infoLabel;
    @FXML
    private Label warnLabel;
    @FXML
    private TextField amountTxt;
    @FXML
    private TextField tarAccNumTxt;


    //variables got from main screen
    private Double balance;
    private String accNumber;
    private String tarAcc;
    private String tarAccName;
    HashMap<String, Account> accs = new HashMap<>();
    LinkedList<String> transactions = new LinkedList<>();
    DecimalFormat df = new DecimalFormat("###.#");

    //transfer button method
    public void transferButtonClick(){

        //connect database for get target account informations
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyAcc = "SELECT accnumber, name FROM customers WHERE accnumber = '" + tarAccNumTxt.getText() + "' ";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyAcc);
            PreparedStatement pst = connectDB.prepareStatement(verifyAcc);
            pst.execute();
            ResultSet rs = pst.getResultSet();

            if (queryResult.next()){

                while (rs.next()){
                   tarAccName = rs.getString(2);
                }

                tarAcc = tarAccNumTxt.getText();

                if(amountTxt.getText().matches("[0-9]+")){

                    double amount = Double.parseDouble(amountTxt.getText());

                    //determine rules for transfer
                    if((amount > balance) && (amount > 10000.0)){
                        warnLabel.setTextFill(Color.color(1, 0, 0));
                        warnLabel.setText("You can not transfer more money than your balance.");
                        amountTxt.setText("");
                    }else if(amount > 10000.0){
                        warnLabel.setTextFill(Color.color(1, 0, 0));
                        warnLabel.setText("You can not transfer more than 10000  $ at once.");
                        amountTxt.setText("");
                    }else{

                        //transfer method in account object with which variables got from main screen
                        accs.get(accNumber).transfer(amount, tarAcc);
                        balance  -= amount;

                        //add new transaction with which same linked list in main screen
                        Transactions ts = new Transactions("Transfer", accNumber,accs.get(accNumber).getName(), (amount + "(amount to : )" + accs.get(accNumber).getName()));
                        transactions.add(ts.toString());

                        //update label
                        infoLabel.setText(amount + "  $  has been transferred.\nTarget account name  :  " + tarAccName +".\nNew balance   :   $" + df.format(balance) );
                        warnLabel.setTextFill(Color.color(0, 1, 0));
                        warnLabel.setText("Transfer completed succesfully.");
                        tarAccNumTxt.setText("");
                        amountTxt.setText("");
                        accs.get(accNumber).setBalance(this.balance);
                    }

                }else{
                    warnLabel.setTextFill(Color.color(1, 0, 0));
                    warnLabel.setText("You must enter just numbers.");
                    amountTxt.setText("");
                }
            }else{
                warnLabel.setTextFill(Color.color(1, 0, 0));
                warnLabel.setText("The Account ID you entered was not found.");
                tarAccNumTxt.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
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
