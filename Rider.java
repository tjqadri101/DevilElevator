import java.io.*;
import java.util.Scanner;


public class Rider implements Runnable {
	private Scanner s;
	
	int riderId;
	Building building;
	public Rider(int i) { riderId =i;} 
	@Override 
	public void run() {
		// TODO Auto-generated method stub
		try {
			s = new Scanner(new File("input.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numFloors=s.nextInt();  //probably do not need the info here
		int numElevators = s.nextInt();
		int numRiders=s.nextInt();
		int maxOccupancy=s.nextInt();
		while(s.hasNextInt()){
			int rider = s.nextInt();
			int startingFloor = s.nextInt();
			int destinationFloor = s.nextInt();
			if (rider == riderId){
				if(startingFloor<destinationFloor){
					Elevator e = building.CallUp(startingFloor);
					e.Enter(rider,startingFloor,2); //need to change this, put parameters floor and direction
					e.RequestFloor(destinationFloor,rider);
					e.Exit(rider,destinationFloor); //put parameters
				}
				if(startingFloor>destinationFloor){
					Elevator e = building.CallDown(startingFloor);
					e.Enter(rider,startingFloor,1);
					e.RequestFloor(destinationFloor,rider);
					e.Exit(rider,destinationFloor);
				}
			}
		}
		s.close();
		
	}

}
