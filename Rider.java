import java.io.*;
import java.util.Scanner;


public class Rider implements Runnable {
	private Scanner myScanner;
	int myRiderId;
	private Building myBuilding;
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
			myScanner.nextInt();
			iter++;
		}
		while(myScanner.hasNextInt()){
			int rider = myScanner.nextInt();
			int startingFloor = myScanner.nextInt();
			int destinationFloor = myScanner.nextInt();
			if (rider == myRiderId){
				if(startingFloor<destinationFloor){
					Elevator e = myBuilding.CallUp(startingFloor, myRiderId);
					while(!e.Enter(rider,startingFloor,2)){
						e = myBuilding.CallUp(startingFloor, myRiderId);
					} //need to change this, put parameters floor and direction
					e.RequestFloor(destinationFloor,rider);
					e.Exit(rider,destinationFloor); //put parameters
					//System.out.println("gets here through some fucking miracle");
				}
				else if(startingFloor>destinationFloor){
					Elevator e = myBuilding.CallDown(startingFloor, myRiderId);
					while(!e.Enter(rider,startingFloor,1)){
						e = myBuilding.CallDown(startingFloor, myRiderId);
					}
					e.RequestFloor(destinationFloor,rider);
					e.Exit(rider,destinationFloor);
					//System.out.println("gets here through some fucking miracle");
				}
			}
		}
		myScanner.close();
		
	}

}
