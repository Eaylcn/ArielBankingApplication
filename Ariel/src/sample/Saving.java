package sample;

/**
 *
 * @author emirata
 */
public class Saving extends Account{
	//List of specific Saving Account properties
	private int safetyDepositBoxID;
	private int safetyDepositBoxKey;
	
	//Constructor to initialize settings for the Saving Accounts
	public Saving(String name, String idNum) {
		super(name, idNum);
                typ = "2";
		accountNumber = typ + accountNumber;
		setSafetyDepositBox();
		//showInfo();
	}

	//constructor to get saving account
	public Saving(String name, String accNum, Double balance, int safetyDepositBoxID, int safetyDepositBoxKey){
		super(name, balance, accNum);
		setSafetyDepositBoxID(safetyDepositBoxID);
		setSafetyDepositBoxKey(safetyDepositBoxKey);
		setRate();
	}

	//functions
	public void setSafetyDepositBoxID(int safetyDepositBoxID){
		this.safetyDepositBoxID = safetyDepositBoxID;
	}
	public void setSafetyDepositBoxKey(int safetyDepositBoxKey){
		this.safetyDepositBoxKey = safetyDepositBoxKey;
	}
	
	@Override
	public void setRate() {
		rate = baseRate - .25;
	}
	
	private void setSafetyDepositBox() {
		safetyDepositBoxID = createNumberWithGivenDigits(3);
		safetyDepositBoxKey = createNumberWithGivenDigits(4);
	}
        public String toString(){
              return "\n------------------------------" +
                        "\nAccount Type : Saving\n" + 
                        super.toString() +
		      "\n-----Your Saving features-----"
                        + "\nSafety Deposit Box ID : " + safetyDepositBoxID
			+ "\nSafety Deposit Box Key : " + safetyDepositBoxKey +
                      "\n------------------------------" +
                      "\nPlease note all informations for would'nt forget ^^";
        }

	@Override
	public void setDebitCardPIN(int pin) {
	}

	@Override
	public int getDebitCardPIN() {
		return 0;
	}

	public int getSafetyDepositBoxID() {
		return safetyDepositBoxID;
	}

	public int getSafetyDepositBoxKey() {
		return safetyDepositBoxKey;
	}
}
