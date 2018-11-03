public class User {
	private String registerd;
	private String[] friends; 
	private int age; 
    private int balance;
    private boolean isActive;
    private String gender;
    private String greeting;

	String getRegistered(){ return registerd; }
	String[] getFriends(){ return friends;}
	int getAge(){ return age; }
    int getBalance(){ return balance; }
    boolean getIsActive(){ return isActive; }
    String getGender(){ return gender; }
    String getGreeting(){ return greeting; }




    @Override
    public String toString() {
        return "User{" + "age=" + age + ", balance=" + balance + 
        ", isActive=" + isActive + ", gender=" + gender + 
        ", greeting=" + greeting +'}';
    }
}
 