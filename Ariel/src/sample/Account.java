package sample;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author emirata
 */
public abstract class Account implements Serializable {
	//variables for saving and checking accounts
	private String name;
	private String idN;
	private Double balance;
    protected String password;
    public String typ;
	protected String accountNumber;
	protected double rate;
	double baseRate = 2.5;

        
	//Constructor to set base properties and initialize the account
	public Account(String name, String idNum) {
		this.name = name;
		this.idN = idNum;
		setRate();

		//create Account Number
		this.accountNumber = createAccountNumber();
		//create Password for login bank
		this.password = createPw();
		
	}

	//Constructor to get account
	public Account(String name, Double balance, String accNum){
		this.name = name;
		accountNumber = accNum;
		this.balance = balance;
	}

	public void setPassword(String pw) {

		//connect database
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB = connectNow.getConnection();

		try {
			PreparedStatement pst = connectDB.prepareStatement("UPDATE accounts SET password =  ? WHERE accountnumber = " + getAccountNumber() + "");
			pst.setString(1, pw);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}



	}

	public void deposit(Double balance){
		//connect database
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB = connectNow.getConnection();
		//Update balance
		this.balance += balance;

		try {
			PreparedStatement pst = connectDB.prepareStatement("UPDATE customers SET balance = balance + " + balance + " WHERE accnumber = " + getAccountNumber() + "");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void withdraw(Double balance){
		//connect database
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB = connectNow.getConnection();
		//Update balance
		this.balance -= balance;

		try {
			PreparedStatement pst = connectDB.prepareStatement("UPDATE customers SET balance = balance - " + balance + " WHERE accnumber = " + getAccountNumber() + "");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void transfer(Double balance , String tarAccNumber){
		//connect database
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB = connectNow.getConnection();
		//Update balance
		this.balance -= balance;

		try {
			PreparedStatement pst = connectDB.prepareStatement("UPDATE customers SET balance = balance - " + balance + " WHERE accnumber = " + getAccountNumber() + "");
			pst.executeUpdate();
			pst = connectDB.prepareStatement("UPDATE customers SET balance = balance + " + balance + " WHERE accnumber = " + tarAccNumber + "");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void creditCardInfos(int newPin, String accNumber){
		//connect database
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB = connectNow.getConnection();

		//Update pin
		try {
			PreparedStatement pst = connectDB.prepareStatement("UPDATE customers SET dbtcardpin = " + newPin + " WHERE accnumber = " + getAccountNumber() + "");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void moneyBoxInfos(int newPin, String accNumber){
		//connect database
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB = connectNow.getConnection();

		//Update pin
		try {
			PreparedStatement pst = connectDB.prepareStatement("UPDATE customers SET safetyboxpin = " + newPin + " WHERE accnumber = " + getAccountNumber() + "");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//abstract method for 2 account types
	public abstract void setRate();
	
	private String createAccountNumber() {
		String lastTwoOfSSN = idN.substring(idN.length()-2, idN.length());
		int uniqueID = createNumberWithGivenDigits(5);
		int randomNumberW3Digits = createNumberWithGivenDigits(3);
		return lastTwoOfSSN + uniqueID + randomNumberW3Digits;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
        public String toString(){
		return ("Name : " + name 
			 + "\nAccount Number : " + accountNumber
                        + "\nYour password is : " + password);
        }
        
        public String createPw(){
            String lastTwoOfSSN = idN.substring(idN.length()-2, idN.length());
            int pw = createNumberWithGivenDigits(6);
            return pw + lastTwoOfSSN;
        }

        public int createNumberWithGivenDigits(int howManyDigits) {
		boolean found = false;
		int a = 0;
		while(!found) {
                    a = (int)(Math.random() * Math.pow(10, howManyDigits));
                    if(a >= Math.pow(10, howManyDigits - 1)) found = true;
		}
		return a;
	}

    public abstract void setDebitCardPIN(int pin);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public abstract int getDebitCardPIN();

	public abstract void setSafetyDepositBoxKey(int pin);

	public abstract int getSafetyDepositBoxKey();

	public abstract int getSafetyDepositBoxID();

	public double getRate() {
		return rate;
	}
}
