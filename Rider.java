import java.io.*;
import java.util.Scanner;


public class Rider implements Runnable {
	private Scanner myScanner;
	int myRiderId;
	Building myBuilding;
	public Rider(int riderId, File input, Building building) {
			myRiderId = riderId;
			myBuilding = building;
			try {
				myScanner = new Scanner(input);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	} 
	@Override 
	public void run() {
		int iter = 0;
		while(iter < 4){//move past the first line which contains four tokens
			myScanner.hasNextInt();
			iter++;
		}
		while(myScanner.hasNextInt()){
			int rider = myScanner.nextInt();
			int startingFloor = myScanner.nextInt();
			int destinationFloor = myScanner.nextInt();
			if (rider == myRiderId){
				if(startingFloor<destinationFloor){
					Elevator e = myBuilding.CallUp(startingFloor);
					e.Enter(rider,startingFloor,2); //need to change this, put parameters floor and direction
					e.RequestFloor(destinationFloor,rider);
					e.Exit(rider,destinationFloor); //put parameters
				}
				else if(startingFloor>destinationFloor){
					Elevator e = myBuilding.CallDown(startingFloor);
					e.Enter(rider,startingFloor,1);
					e.RequestFloor(destinationFloor,rider);
					e.Exit(rider,destinationFloor);
				}
			}
		}
		myScanner.close();
		
	}

}
