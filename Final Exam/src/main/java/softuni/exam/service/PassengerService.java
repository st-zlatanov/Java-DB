package softuni.exam.service;


import java.io.FileNotFoundException;
import java.io.IOException;


public interface PassengerService {

    boolean areImported();

    String readPassengersFileContent() throws IOException;
	
	String importPassengers() throws FileNotFoundException;

    String getPassengersOrderByTicketsCountDescendingThenByEmail();
}
