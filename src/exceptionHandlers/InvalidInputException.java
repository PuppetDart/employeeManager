package exceptionHandlers;

public class InvalidInputException extends Exception{
	public InvalidInputException() {
		super();
	}
	
	public void invalid(String entry, String entity) {
		System.out.println("'"+entry +"' is an invalid input.");
		System.out.println();
	}
}
