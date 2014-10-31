//Tests the first part and the second part of the Elevator assignment
//If the number of elevators specified in the input file is greater than 1 then
//error message is logged and TestP1 returns without doing anything
//For part 1, to make the elevator hold any capacity, simple make the ...
//maxOccuppany paramater(N) in input file greater than the number of riders (R)
//If R > N, then this program can be used to test for part 2
import java.io.*;
import java.util.*;

public class TestP1P2 {
		public static void runTest(String inputFilePath, boolean p1p2){
			Elevator[] elevatorSet;
			Rider[] riderSet;
			Thread[] elevatorTs;
			Thread[] riderTs;
			int numFloors = 0, numElevators = 0, numRiders = 0, maxOccupancy = 0;
			File inputFile = new File(inputFilePath);
			try {
				Scanner mainScanner = new Scanner(inputFile);
				 numFloors = mainScanner.nextInt();  //probably do not need the info here
				 numElevators = mainScanner.nextInt();
				 numRiders = mainScanner.nextInt();
				 maxOccupancy = mainScanner.nextInt();
				 mainScanner.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.printf("F: %d, E: %d, R: %d, N: %d \n", numFloors, numElevators, numRiders, maxOccupancy);
			if(numElevators > 1 && p1p2){
				System.out.println("You are testing for part 1 and part 2. The number of elevators cannot exceed 1");
				return;
			}
			MyLogger logger = new MyLogger();
			elevatorSet = new Elevator[numElevators];
			elevatorTs = new Thread[numElevators];
			for(int i = 0; i < numElevators; i++){
				elevatorSet[i] = new Elevator(numFloors, i+1, maxOccupancy, logger);
				elevatorTs[i] = new Thread(elevatorSet[i]);
			}
			
			Building building = new Building(numFloors, numElevators, numRiders,elevatorSet, logger);
			
			riderSet = new Rider[numRiders];
			riderTs = new Thread[numRiders];
			for(int j = 0; j < numRiders; j++){
				riderSet[j] = new Rider(j+1, inputFile, building);
				riderTs[j] = new Thread(riderSet[j]);
			}
			
			for(int i = 0; i < numElevators; i++){
				elevatorTs[i].start();
			}
			
			for(int j = 0; j < numRiders; j++){
				riderTs[j].start();
			}
			
		}
		
		public static void main(String args[]){
			runTest("inP1.txt", false);
		}
}
