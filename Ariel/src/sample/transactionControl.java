package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class transactionControl implements Initializable {

    //gui variables
    @FXML
    private ListView<String> transactionList;
    @FXML
    private ImageView closeButton;

    //Linked list got from main screen
    LinkedList<String> transactions = new LinkedList<>();

    //set the listview with linked list items
    public void setList(){

        //iterator for get all item in linked list
        Iterator<String> iterator = transactions.iterator();
        while(iterator.hasNext()){
            transactionList.getItems().addAll(iterator.next());
        }
    }

    //close button
    public void closeThisGoMain(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setTransactions(LinkedList<String> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File closeBtnFile = new File("images/closebutton2.png");
        Image closeImage = new Image(closeBtnFile.toURI().toString());
        closeButton.setImage(closeImage);

    }
}
