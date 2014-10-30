//Tests the first part of the Elevator
import java.io.*;
import java.util.*;

public class TestP1 {
		public static void runTest(String inputFilePath){
			int numFloors, numElevators, numRiders, maxOccupancy;
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
			
			
		}
}
