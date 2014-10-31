


public class Main {
	public static void main(String args[]){
		if(args.length == 0) {
			// No options specified; make the default as the part 3 elevator submission
			TestP1P2.runTest("inP1.txt", false);
		} else if(args.length > 1) {
			// Throw an error--too many args
			System.out.println("Too many arguments");
			System.exit(1);
		} else{ // known just one arg
			if(args[0].equals("p1")){
				// call the EventBarrier"
				TestEventBarrier.test();
			}
			if(args[0].equals("p2part1")) {
				// call the elevator part1
				TestP1P2.runTest("inP1.txt", true);
			} else if(args[0].equals("p2part2")) {
				// call the elevator part2
				TestP1P2.runTest("inP1.txt", true);
			} else if(args[0].equals("p2part3")) {
				// call the elevator part3
				TestP1P2.runTest("inP1.txt", false);
			}
		}
	}
}
