package sample;

/**
 *
 * @author emirata
 */
public class Checking extends Account {
	//List of specific Checking Account properties
	private String debitCardNumber;
	private int debitCardPIN;
	private final static int debitCardFirst4Digit  = 1610;
	
	//Constructor to initialize settings for the Checking Accounts
	public Checking(String name, String idNum) {
		super(name, idNum);
                typ = "1";
		accountNumber = typ + accountNumber;
		setDebitCard();
		//showInfo();
	}

	//constructor to set checking account
	public Checking(String name, String accNum, Double balance, String dbtCardNumber, int debitCardPIN){
		super(name, balance, accNum);
		debitCardNumber = dbtCardNumber;
		this.debitCardPIN = debitCardPIN;
		setRate();
	}

	@Override
	public void setRate() {
		rate = baseRate * .15;
	}
	
	private void setDebitCard() {
		Integer[] digits = new Integer[3];
		for(int i = 0; i < digits.length; i++) {
			digits[i] = createNumberWithGivenDigits(4);
		}
		debitCardNumber = debitCardFirst4Digit + " " + digits[0] + " " + digits[1] + " " + digits[2];
		debitCardPIN = createNumberWithGivenDigits(4);
	}
        
        public String toString(){
              return "\n------------------------------" +
                        "\nAccount Type : Checking\n" + 
                        super.toString() +
		      "\n----Your Checking Features----" 
                    + "\nDebit Card Number : " + debitCardNumber
                    + "\nDebit Card PIN : " + debitCardPIN +
		        "\n------------------------------" +
                      "\nPlease note all informations for would'nt forget ^^";
        }

	public String getDebitCardNumber() {
		return debitCardNumber;
	}

	public int getDebitCardPIN() {
		return debitCardPIN;
	}

	@Override
	public void setSafetyDepositBoxKey(int pin) {

	}

	@Override
	public int getSafetyDepositBoxKey() {
		return 0;
	}

	@Override
	public int getSafetyDepositBoxID() {
		return 0;
	}

	public void setDebitCardPIN(int debitCardPIN) {
		this.debitCardPIN = debitCardPIN;
	}
}
