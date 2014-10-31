/**********************************************
 * Please DO NOT MODIFY the format of this file
 **********************************************/

/*************************
 * Team Info & Time spent
 *************************/

	Name1: Talal Javed Qadri	// Edit this accordingly
	NetId1: tq4	 	// Edit
	Time spent: 25 hours 	// Edit 

	Name2: Alex Milu 	// Edit this accordingly
	NetId2: aam47	 	// Edit
	Time spent: 25 hours 	// Edit 


    Note: Output is logged to a file called output.txt. Also note that our jar takes input
    from input.txt so please copy the contents of your external input files to input.txt before executing the jar
    
/******************
 * Files submitted
 ******************/

	We submitted an executable lab3.jar 
    We also submitted a zipped file including all the source files and test cases.
	README	// This file filled with the lab implementation details
	Elevator.input // You can submit a sample input and log file
    Elevator logs to output.txt. The output corresponds to the input file
    but Elevator logs to output.txt by auto-generation via execution of jar file

/************************
 * Implementation details
 *************************/

/* 
 * This section should contain the implementation details and a overview of the
 * results. 

 * You are required to provide a good README document including the
 * implementation details. In particular, you can use pseudocode to describe
 * your implementation details where necessary. However that does not mean to
 * copy/paste your C code. Specifically, explain the synchronization primities
 * used in implmenting the elevator, scheduling choices used, how the capacity
 * constraint is handled, and how the mulitple elevators are supported. Also,
 * explain how to run the test cases for your EventBarrier and Elevator and how
 * to interpret the results. Check the README.OUTPUTFORMAT for the acceptable
 * input/output format for the elevator. Expect the design and implementation
 * details to be at most 2-3 pages.  A plain textfile is encouraged. However, a
 * pdf is acceptable.  No other forms are permitted.

 * In case of lab is limited in some functionality, you should provide the
 * details to maximize your partial credit.  
 * */
 
 In our EventBarrier, after a thread calls arrive and the barrier is down(event=false),
 it is put to sleep (by calling wait()) until the event barrier calls raise() which after it makes
 event=true (opens the barrier), notifies (notifyAll()) the threads that arrived that they 
 can continue. Before the raise() method exits we close the barrier by making event=false again. 
 In the complete method if there is a thread that has not completed yet, we make the current 
 thread wait. When all the threads have finished we call notifyAll() so the threads that were 
 waiting wake up.  
 
 
 
 In our implementation for the elevator part, the elevator threads and rider threads would wait on some conditions. It
 all begins with a rider thread calling (CallUp or CallDown) an elevator. Then this rider thread
 calls enter which makes it wait (wait()) for an elevator to come at its floor. When the elevator
 thread calls VisitFloor then if it has someone to pick up it calls OpenDoors. This method notifies
 the threads that were waiting for an elevator. Then the elevator calls ClosedDoors which waits
 for all the riders to enter or exit the elevator. In our implementation we make the riders who
 enter wait for the riders who need to exit. After the elevator thread calls ClosedDoors it goes on
 and performs the next request. For a rider thread, after it calls Enter it calls RequestFloor. Notice
 that if a rider does not request a floor nothing bad happens, the elevator will  just become
 idle if it does not have ay other requests. After it has called RequestFloor, 
it then waits to exit the elevator by calling Exit, which 
 makes it wait() until the elevator opens doors at the desired floor. After the elevator opens doors at 
 the desired floor (by calling OpenDoors) the rider thread gets notified and exits the elevator. 
 
 For the scheduling of the elevators we used the circular SCAN algorithm. The elevator starts from a
 floor and goes in one direction (up or down) until it has no more requests or he reached the top
 or bottom floor. When an elevator has no more requests, we make it idle and we put the elevator
 thread to sleep (by calling wait()) until it gets a new request. 
 
 
 To handle the capacity constraint we added a variable occupancy that gets increased when a 
 person enters the elevator. When occupancy reaches maxoccupancy we do not let people in anymore
 and the elevator can close the doors. When a rider is rejected (the enter method returns false) 
 we put the rider thread to sleep (by calling sleep()) so it will not call another elevator
 immediately. We did this in order to simulate realistically the interaction between the riders
 and elevators. Calling sleep() can be undone by commenting out the "Thread.sleep" in the rider class. 
 The only impact on performance will be that the rider will continue to call an elevator
 and thus it will not let the elevator to leave. When in the real world this happens the rider
 who does this is always admonished by the other passengers. 
 
 For multiple elevators, our policy assigns to a particular rider the elevator that is 
 closest to it and going in the right direction. The elevator gets to know he has a request at 
 floor  i when we increment the variable FloorRequestsIn(Up/Down)[i] after a rider has called up or down 
 from floor i. When picking the closest elevator, if there is no elevator going into the right
 direction we pick the elevator that would have the to do the least number of steps before it
 gets to the rider (i.e. the elevator has to go to the top floor or bottom floor and then come 
 back).    
 
 
To test it, for EventBarrier we create a number of threads that will call arrive on the barrier. 
Each thread can call arrive a fixed number of times. After a thread calls arrive and before
it completes it does some useless work so it does not complete immediately. In our case
it just goes through a for loop and prints and integer after the loop is over. In this way
we make it seem like the consumer takes some time before crossing the bridge.  

For Elevator part 1, we test it by making N greater than the number of riders (in this way
we mimic infinite maximum occupancy) and E=1. For part 2, we test it by making N smaller than 
the number of riders (it is best to make it 1 so you can have a lot of conflicts getting 
in the elevator. For part three we make E>1.


We have included an executable jar file to help you run the program. If no arguments are provided,
then the jar executes TestPart3 by default. If more than 1 arguments are provided, then an error message is
displayed on the console and the program exits. If, argument p1 is provided, then the program executes the test for
event barrier (TestEventBarrier). For the elevator, we have one test class called TestP1P2. If, the jar is run with
the arguments p2part1 or p2part2, then TestP1P2 runs with a constraint that the number of elevators is 1. If the number 
of elevators exceeds 1, then the program simply displays an error message on the console and quits. To test for part 1, just
make the capacity (specified in the input file) larger than the number of riders. To test for part3, the program needs
an argument of p2part3. Our program expects valid input and doesn't check for invalid input errors as this was not required.
Thus, to test for part 1 make sure the capacity is greater than the number of riders. For part 2, make sure the capacity is less
than the number of riders, and for part 3 make sure the number of elevators is greater than 1. You need to copy the contents of your
external input file to input.txt before running the program. The output is logged to output.txt.




/************************
 * Feedback on the lab
 ************************/

/*
 * Any comments/questions/suggestions/experiences that you would help us to
 * improve the lab.
 * */
 The lab was interesting and useful in understanding concurrency.

/************************
 * References
 ************************/

/*
 * List of collaborators involved including any online references/citations.
 * */
We used some java documentation available online if we did not know how to code certain actions,
such as writing into a logger.
