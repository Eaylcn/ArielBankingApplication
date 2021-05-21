package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable  {

    //GUI Variables
    @FXML
    private ImageView closeButton;
    @FXML
    private ImageView depositButton;
    @FXML
    private ImageView withdrawButton;
    @FXML
    private ImageView accInfoButton;
    @FXML
    private ImageView transactionButton;
    @FXML
    private ImageView transferButton;
    @FXML
    public ImageView creditCardButton;
    @FXML
    public ImageView moneyBoxButton;
    @FXML
    private ImageView userImage;
    @FXML
    private ImageView background;
    @FXML
    private Label nameLabel;
    @FXML
    private Label infoLabel;

    //account variables
    private String accountNumber;
    private String fullName;
    private String accType;
    private Double balance;
    private String debitCardNum;
    private int debitCardPin;
    private int safetyBoxID;
    private int safetyBoxPin;
    private Account acc;

    //Stage object for control any other stage is active
    private Stage otherStage;

    //Data structures
    public HashMap<String, Account> accounts = new HashMap<>();
    public LinkedList<String> transactions = new LinkedList<>();

    //double to decimal
    DecimalFormat df = new DecimalFormat("###.#");

    //get account from object with true variables
    public void getAccount(){
        if(accType.equals("Saving")){
            acc = new Saving(this.fullName, this.accountNumber, this.balance, this.safetyBoxID, this.safetyBoxPin);
            accounts.put(accountNumber, acc);
        }else{
            acc = new Checking(this.fullName, this.accountNumber, this.balance, this.debitCardNum, this.debitCardPin);
            accounts.put(accountNumber, acc);
        }
    }

    //Button methods
    public void showInfo(){
        if(otherStage == null){

            //connect database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            //variables for new stage
            String email = null;
            String identifynumber = null;
            String birthdate = null;
            String password = null;

            //get values from database with given accountNumber
            String query = "SELECT email, identifynumber, birthdate, password FROM accounts WHERE accountnumber = " + accountNumber + " ;";

            try {
                PreparedStatement pst = connectDB.prepareStatement(query);
                pst.execute();
                ResultSet rs = pst.getResultSet();
                while(rs.next()){
                    email = rs.getString(1);
                    identifynumber = rs.getString(2);
                    birthdate = rs.getString(3);
                    password = rs.getString(4);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //Set the show info screen
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("accountinfos.fxml"));
                Parent root = (Parent) loader.load();

                //Set variables to main stage before it creating
                accountInfosController aic = (accountInfosController) loader.getController();
                aic.setAccNumber(accountNumber);
                aic.setNameLabel(fullName);
                aic.setEmailLabel(email);
                aic.setIdnumLabel(identifynumber);
                aic.setDateLabel(birthdate);
                aic.getPwTxt().setText(password);

                //set conenct with hashmaps for get same account object and access same values always
                aic.setAccs(accounts);

                //set conenct with linkedlist for save any transactions to same list
                aic.setTransactions(transactions);

                //set stage
                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 477, 397));
                otherStage.setResizable(false);
                otherStage.show();

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }else{
            otherStage.toFront();
        }
    }

    public void transactionHistory(){
        if(otherStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));
                Parent root = (Parent) loader.load();

                //set variables before stage is creating
                transactionControl tc = (transactionControl) loader.getController();

                //connect linked list for get transaction history
                tc.setTransactions(transactions);
                tc.setList();

                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 928, 633));
                otherStage.setResizable(false);
                otherStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            otherStage.toFront();
        }
    }

    public void deposit(){
        if(otherStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("depositsection.fxml"));
                Parent root = (Parent) loader.load();
                depositControl dc = (depositControl) loader.getController();

                //Set variables to main screen before it creating
                dc.setInfoLabel("Your Balance   :   " + df.format(accounts.get(accountNumber).getBalance()) + " $");
                dc.setBalance(accounts.get(accountNumber).getBalance());
                dc.setAccNumber(accountNumber);
                dc.setAccs(accounts);
                dc.setTransactions(transactions);

                //set stage
                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 470, 367));
                otherStage.setResizable(false);
                otherStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            otherStage.toFront();
        }
    }

    public void withdraw(){
        if(otherStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("withdrawsection.fxml"));
                Parent root = (Parent) loader.load();
                withdrawControl wc = (withdrawControl) loader.getController();

                //Set variables to main screen before it creating
                wc.setInfoLabel("Your Balance   :   " + df.format(accounts.get(accountNumber).getBalance()) + " $");
                wc.setBalance(accounts.get(accountNumber).getBalance());
                wc.setAccNumber(accountNumber);
                wc.setAccs(accounts);
                wc.setTransactions(transactions);

                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 470, 367));
                otherStage.setResizable(false);
                otherStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            otherStage.toFront();
        }
    }

    public void transfer() {

        if (otherStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("transfersection.fxml"));
                Parent root = (Parent) loader.load();
                transferControl tc = (transferControl) loader.getController();

                //Set variables to main screen before it creating
                tc.setInfoLabel("Your Balance   :   " + df.format(accounts.get(accountNumber).getBalance()) + " $");
                tc.setBalance(accounts.get(accountNumber).getBalance());
                tc.setAccNumber(accountNumber);
                tc.setAccs(accounts);
                tc.setTransactions(transactions);

                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 470, 406));
                otherStage.setResizable(false);
                otherStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }

        }
    }

    public void creditCardButton(){
        if(otherStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("creditcardinfos.fxml"));
                Parent root = (Parent) loader.load();
                CreditCardInfosController ccic = (CreditCardInfosController) loader.getController();

                //Set variables to main screen before it creating
                debitCardPin = accounts.get(accountNumber).getDebitCardPIN();
                ccic.setAccs(accounts);
                ccic.setCcNumLabel(debitCardNum);
                ccic.setCcNameLabel(fullName);
                ccic.setPinTxt(debitCardPin);
                ccic.setPin(debitCardPin);
                ccic.setAccNumber(accountNumber);
                ccic.setTransactions(transactions);

                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 443, 356));
                otherStage.setResizable(false);
                otherStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            otherStage.toFront();
        }
    }

    public void moneyBoxButton(){
        if(otherStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("moneyboxinfos.fxml"));
                Parent root = (Parent) loader.load();
                moneyBoxInfosController mbic = (moneyBoxInfosController) loader.getController();

                //get variables from account object
                safetyBoxPin = accounts.get(accountNumber).getSafetyDepositBoxKey();
                String sSafetyBoxID = String.valueOf(accounts.get(accountNumber).getSafetyDepositBoxID());
                double a =  (accounts.get(accountNumber).getBalance() * accounts.get(accountNumber).getRate()/100);
                String sBalance = String.valueOf(a);

                //Set variables to main screen before it creating
                mbic.setAccs(accounts);
                mbic.setMoneyLabel(sBalance + " $");
                mbic.setBoxIdLabel("Safety box ID : " + sSafetyBoxID);
                mbic.setPinTxt(safetyBoxPin);
                mbic.setPin(safetyBoxPin);
                mbic.setAccNumber(accountNumber);
                mbic.setTransactions(transactions);

                otherStage = new Stage();
                otherStage.setOnHiding(we -> otherStage = null);
                otherStage.setTitle("Ariel Banking Application");
                otherStage.initStyle(StageStyle.UNDECORATED);
                otherStage.setScene(new Scene(root, 443, 414));
                otherStage.setResizable(false);
                otherStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            otherStage.toFront();
        }
    }

    //close method
    public void closeThisGoLogin() {
        if (otherStage == null) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage loginStage = new Stage();
                loginStage.setTitle("Ariel Banking Application");
                //loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.setScene(new Scene(root, 600, 400));
                loginStage.setResizable(false);
                loginStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    //Setters and Getters

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDebitCardNum(String debitCardNum) {
        this.debitCardNum = debitCardNum;
    }

    public void setDebitCardPin(int debitCardPin) {
        this.debitCardPin = debitCardPin;
    }

    public void setSafetyBoxID(int safetyBoxID) {
        this.safetyBoxID = safetyBoxID;
    }

    public void setSafetyBoxPin(int safetyBoxPin) {
        this.safetyBoxPin = safetyBoxPin;
    }

    public void setNameLabel(String string){
        nameLabel.setText(string);
    }

    public void setInfoLabel(String string){
        infoLabel.setText(string);
    }

    public void setInfoLabel(){
        infoLabel.setText(
                "-Your Customer Informations-" +
                        "\nAccount Type : " + accType +
                        "\nAccount ID : " + accountNumber + "\nBalance : " + df.format(accounts.get(accountNumber).getBalance()) + " $");
    }

    //Show Images
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File accInfoBtnFile = new File("images/accinfobutton.png");
        Image accInfoImage = new Image(accInfoBtnFile.toURI().toString());
        accInfoButton.setImage(accInfoImage);
        File closeBtnFile = new File("images/closebutton.png");
        Image closeImage = new Image(closeBtnFile.toURI().toString());
        closeButton.setImage(closeImage);
        File bgFile = new File("images/mainbg.png");
        Image bgImage = new Image(bgFile.toURI().toString());
        background.setImage(bgImage);
        File transferBtnFile = new File("images/transferbutton.png");
        Image transferImage = new Image(transferBtnFile.toURI().toString());
        transferButton.setImage(transferImage);
        File withdrawBtnFile = new File("images/withdrawbutton.png");
        Image withdrawImage = new Image(withdrawBtnFile.toURI().toString());
        withdrawButton.setImage(withdrawImage);
        File creditBtnFile = new File("images/creditcardbutton.png");
        Image creditImage = new Image(creditBtnFile.toURI().toString());
        creditCardButton.setImage(creditImage);
        File moneyBoxBtnFile = new File("images/moneyboxbutton.png");
        Image moneyBoxImage = new Image(moneyBoxBtnFile.toURI().toString());
        moneyBoxButton.setImage(moneyBoxImage);
        File depositBtnFile = new File("images/depositbutton.png");
        Image depositImage = new Image(depositBtnFile.toURI().toString());
        depositButton.setImage(depositImage);
        File userImageFile = new File("images/userpp.png");
        Image userImg = new Image(userImageFile.toURI().toString());
        userImage.setImage(userImg);
        File transactionFile = new File("images/transactions.png");
        Image transactionImage = new Image(transactionFile.toURI().toString());
        transactionButton.setImage(transactionImage);
    }

}
