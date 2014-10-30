//Tests the first part of the Elevator
import java.io.*;
import java.util.*;

public class TestP1 {
		public static void runTest(String inputFilePath){
			int numFloors = 0, numElevators = 0, numRiders = 0, maxOccupancy = 0;
			File input = new File(inputFilePath);
			try {
				Scanner mainScanner = new Scanner(input);
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
		}
		
		public static void main(String args[]){
			runTest("in.txt");
		}
}
