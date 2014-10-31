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


    Note: Output is logged to a file called output.txt
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
 
 In our implementation, the elevator threads and rider threads would wait on some conditions. While
 waiting they will be notified by the calling the notifyAll() in some other part of the code.Since
 multiple threads might be sleeping we used notifyAll() instead of notify(). We are using a circular
 scan, that is while our direction is up and we have requests we go up until we reach the top floor.
 There we change the direction and start to go down. Notice that the elevator becomes idle if there 
 are no requests at one moment, it does not continue to go up or down. To handle the capacity
 constraint we added a variable occupancy that gets increased when a person enters the elevator.
 When occupancy reaches maxoccupancy we do not let people in anymore and the elevator can close the
 doors. When a rider is rejected (the enter method returns false) we put the rider thread to sleep 
 so it will not call another elevator immediately. 

/************************
 * Feedback on the lab
 ************************/

/*
 * Any comments/questions/suggestions/experiences that you would help us to
 * improve the lab.
 * */

/************************
 * References
 ************************/

/*
 * List of collaborators involved including any online references/citations.
 * */
