package sample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transactions {

    //transactions variables
    private static int id = 0;
    private String transactionType;
    private String userName;
    private String accountNumber;
    private String date;
    private String target;

    //Constructor to set base properties and initialize the account
    public Transactions(String transactionType, String accountNumber, String userName, String target){
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
        this.userName = userName;
        this.target = target;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date = dtf.format(now);

        id++;
    }

    // to string method for get transactions with string
    public String toString(){
        return "ID : " + id + " Account Number :    " + accountNumber + "\tTransaction Type :    " + transactionType + "\tDate :    " + date  + "\tValue : " + target;
    }

}
