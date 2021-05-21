package sample;

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
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    //GUI Variables
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField accIDTxt;
    @FXML
    private PasswordField passwordTxt;

    //for decimal format
    DecimalFormat df = new DecimalFormat("###.#");

    //Control given inputs
    public void validateLogin(){
        //Connect database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM accounts WHERE accountnumber = '" + accIDTxt.getText() + "' AND password = '" + passwordTxt.getText() + "' ";

        try{
            //Checking the given data match the database
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    loginMessageLabel.setTextFill(Color.color(0, 1, 0));
                    loginMessageLabel.setText("Congrats you are loginning in...");

                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();

                    prepareMainScreen();
                }else{
                    loginMessageLabel.setTextFill(Color.color(1, 0, 0));
                    loginMessageLabel.setText("Invalid username or password.\nPlease try again.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    //Method for prepare main screen if given inputs is true
    public void prepareMainScreen(){
        //Conenct Database for get customer informations
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        //Customer Variables
        String accType = null;
        String fullName = null;
        double balance = 0;
        String dbtCardNum = null;
        int dbtCardPin = 0;
        int safetyBoxID = 0;
        int safetyBoxPin = 0;
        boolean isSaving = false;

        //Select variable from database
        String query = "SELECT acctype FROM customers WHERE accnumber = " + accIDTxt.getText() + " ;";

        try {
            PreparedStatement pst = connectDB.prepareStatement(query);
            pst.execute();
            ResultSet rs = pst.getResultSet();
            while(rs.next()){
                accType = rs.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Determine which query need to be select
        if(accType.equals("Saving")) {
            query = "SELECT name, balance, safetyboxid, safetyboxpin FROM customers WHERE accnumber = " + accIDTxt.getText() + " ;";
            isSaving = true;
        } else{
            query = "SELECT name, balance, dbtcardnum, dbtcardpin FROM customers WHERE accnumber = " + accIDTxt.getText() + " ;";
        }

        //Assign the given values from database to program
        try {
            PreparedStatement pst = connectDB.prepareStatement(query);
            pst.execute();
            ResultSet rs = pst.getResultSet();
            while(rs.next()){
                fullName = rs.getString(1);
                balance = rs.getDouble(2);
                if(isSaving){
                    safetyBoxID = rs.getInt(3);
                    safetyBoxPin = rs.getInt(4);
                }else{
                    dbtCardNum = rs.getString(3);
                    dbtCardPin = rs.getInt(4);
                }


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Set screen for main screen
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
            Parent root = (Parent) loader.load();

            //Set variables to main screen before it creating
            MainScreenController msc = (MainScreenController) loader.getController();
            msc.setFullName(fullName);
            msc.setAccountNumber(accIDTxt.getText());
            msc.setBalance(balance);
            msc.setAccType(accType);
            msc.setNameLabel("Welcome, " + fullName);
            msc.setInfoLabel("-Your Customer Informations-" +
                    "\nAccount Type : " + accType +
                    "\nAccount ID : " + accIDTxt.getText() +
                    "\nBalance : " + df.format(balance) + " $");

            if(isSaving){
                msc.setSafetyBoxID(safetyBoxID);
                msc.setSafetyBoxPin(safetyBoxPin);
                msc.moneyBoxButton.setVisible(true);
            }else{
                msc.setDebitCardNum(dbtCardNum);
                msc.setDebitCardPin(dbtCardPin);
                msc.creditCardButton.setVisible(true);
            }

            //Create account on main screen with given values
            msc.getAccount();

            //Create stage
            Stage mainStage = new Stage();
            mainStage.setTitle("Ariel Banking Application");
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setScene(new Scene(root, 1056, 662));
            mainStage.setResizable(false);
            mainStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    //Button methods
    public void registerButtonOnAction(ActionEvent event){
        //Close this section
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        //Open register section
        try{
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("Ariel Banking Application");
            registerStage.initStyle(StageStyle.UNDECORATED);

            registerStage.setScene(new Scene(root, 450, 610));
            registerStage.setResizable(false);
            registerStage.show();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void loginButtonOnAction(ActionEvent event){
        //Get given inputs
        if((accIDTxt.getText().isBlank() == false) && (passwordTxt.getText().isBlank() == false)){
            loginMessageLabel.setTextFill(Color.color(0, 1, 0));
            loginMessageLabel.setText("You try to login...");
            validateLogin();
        }else{
            loginMessageLabel.setTextFill(Color.color(1, 0, 0));
            loginMessageLabel.setText("Please enter username and password");
        }

    }


    //Show Images
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/ArielLogin.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("images/lockLogo.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }
}
