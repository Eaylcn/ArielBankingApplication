package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {


    //GUI Variables
    @FXML
    private ImageView formImageView;
    @FXML
    private Button backButton;
    @FXML
    private Label infoLabel;
    @FXML
    private PasswordField pwTxt;
    @FXML
    private PasswordField confirmPwTxt;
    @FXML
    private TextField firstNameTxt;
    @FXML
    private TextField lastNameTxt;
    @FXML
    private TextField emailTxt;
    @FXML
    private CheckBox aggreCheckBox;
    @FXML
    private ComboBox accTypeCombo;
    @FXML
    private TextField idNumTxt;
    @FXML
    private DatePicker birthDatePicker;

    //Button methods
    public void registerBtnOnAction(ActionEvent event) {
        //Control is any inputs is blank
        if((firstNameTxt.getText().isEmpty() == false) && (lastNameTxt.getText().isEmpty() == false)
            && (idNumTxt.getText().isEmpty() == false) && (birthDatePicker.getValue() != null)
            && (emailTxt.getText().isEmpty() == false) && (accTypeCombo.getValue().toString().isEmpty() == false)){
            if(aggreCheckBox.isSelected()){
                if(confirmPassword()){
                    registerUser();
                    infoLabel.setTextFill(Color.color(0, 1, 0));
                    infoLabel.setText("Your account has been created.");
                }
            }else{
                infoLabel.setTextFill(Color.color(1, 0, 0));
                infoLabel.setText("Please accept the terms of user aggreement.");
            }

        }else {
            infoLabel.setTextFill(Color.color(1, 0, 0));
            infoLabel.setText("Please enter your informations.");
        }
    }

    public void backButtonOnAction(ActionEvent event){
        closeThisGoLogin();
    }

    //Method for is match passwords
    public boolean confirmPassword(){
        if(pwTxt.getText().equals(confirmPwTxt.getText())){
            infoLabel.setText("");
            return true;
        }else{
            infoLabel.setText("Passwords does not match...");
            pwTxt.setText("");
            confirmPwTxt.setText("");
            return false;
        }
    }

    //Method for register user with given informations
    public void registerUser(){
        //Connect database for create user on database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        //Get inputs
        String firstname = firstNameTxt.getText();
        String lastname = lastNameTxt.getText();
        String fullName = firstname + " " + lastname;
        String email = emailTxt.getText();
        String password = pwTxt.getText();
        String identifynumber = idNumTxt.getText();
        String birthdate = birthDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String accounttype = accTypeCombo.getValue().toString();
        String accountnumber = "1";

        //Determine which query need
        if(accounttype.equals("Saving")){
            //Create account object for first time
            Account savAcc = new Saving(fullName, identifynumber);
            try {
                PreparedStatement pst = connectDB.prepareStatement("INSERT INTO customers (acctype, accnumber, name, balance, safetyboxid, safetyboxpin) VALUES (?,?,?,?,?,?)");

                //get created informations from account object and set for database them
                pst.setString(1,"Saving");
                pst.setString(2, savAcc.getAccountNumber());
                pst.setString(3, fullName);
                pst.setInt(4, 0);
                pst.setInt(5, ((Saving) savAcc).getSafetyDepositBoxID());
                pst.setInt(6, ((Saving) savAcc).getSafetyDepositBoxKey());
                pst.executeUpdate();
                pst.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            accountnumber = savAcc.getAccountNumber();
        }else{
            //Create account object for first time
            Account chkAcc = new Checking(fullName, identifynumber);
            try {
                PreparedStatement pst = connectDB.prepareStatement("insert into customers (acctype, accnumber, name, balance, dbtcardnum, dbtcardpin) values (?,?,?,?,?,?)");

                //get created informations from account object and set for database them
                pst.setString(1,"Checking");
                pst.setString(2, chkAcc.getAccountNumber());
                pst.setString(3, fullName);
                pst.setInt(4, 0);
                pst.setString(5, ((Checking) chkAcc).getDebitCardNumber());
                pst.setInt(6, ((Checking) chkAcc).getDebitCardPIN());
                pst.executeUpdate();
                pst.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            accountnumber = chkAcc.getAccountNumber();
        }

        //create registered users infos to database
        String insertToRegister = "INSERT INTO accounts (accountnumber, password, firstname, lastname, identifynumber, email, birthdate, accounttype) VALUES('"+ accountnumber + "', '" + password + "', '" + firstname + "', '" + lastname + "', '" + identifynumber + "', '" + email + "', '" + birthdate + "', '" + accounttype + "')";

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);

            //blank all inputs for new register
            firstNameTxt.setText("");
            lastNameTxt.setText("");
            emailTxt.setText("");
            pwTxt.setText("");
            confirmPwTxt.setText("");
            idNumTxt.setText("");
            birthDatePicker.setValue(null);
            accTypeCombo.setValue(null);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }


    }

    // Go login method fot go back button
    public void closeThisGoLogin(){
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();

        try{
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Ariel Banking Application");
            //loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root, 600, 400));
            loginStage.setResizable(false);
            loginStage.show();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    //Show Images
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File formFile = new File("images/form.png");
        Image formImage = new Image(formFile.toURI().toString());
        formImageView.setImage(formImage);
        accTypeCombo.getItems().addAll("Saving", "Checking");
    }

}
