package main.java.ua.nure.bogun.apiproject.database;

public class DBException extends Exception {

	public DBException(String message, Exception ex) {
		super(message, ex);
	}

}
