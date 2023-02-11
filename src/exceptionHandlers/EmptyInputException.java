package exceptionHandlers;

public class EmptyInputException extends Exception{
	public EmptyInputException(String entity) {
		System.out.println("Empty "+ entity + " not allowed.");
	}
}
