
public class Elevator extends AbstractElevator implements Runnable{

	public int currentDirection; //0 idle, 1 down, 2 up
	private boolean openDoors;
	//private int numPeopleIn;
	//private int numPeopleOut;
	private int occupancy;
	private int[] FloorRequestsInUp = new int[numFloors+1]; //no floor at index 0
	private int[] FloorRequestsInDown = new int[numFloors+1];
	private int[] FloorRequestsOut = new int[numFloors+1];//no floor at index 0
	public int totalRequests;
	private int currentFloor;
	private MyLogger myLogger;
	
	/**
	 * Other variables/data structures as needed goes here 
	 */

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, MyLogger logger) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		//Initialize the elevator to start at the first floor
		VisitFloor(1);
		currentDirection = 0; //initialize the elevator to be initially idle
		myLogger = logger;
	}

	@Override
	/**
	 * Elevator control interface: invoked by Elevator thread.
	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors(){
		openDoors = true;
		String message = "E"+elevatorId+" on F"+currentFloor+" opens";
		myLogger.log(message);
		notifyAll();
	}

	@Override
	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 */
	public synchronized void ClosedDoors(){
		assert openDoors;
		//System.out.println("got here some how");
		
		//while(((currentDirection==2) && (FloorRequestsInUp[currentFloor])>0))||(FloorRequestsOut[currentFloor]>0)&&(occupancy<maxOccupancyThreshold)){
		while((FloorRequestsOut[currentFloor]>0) 
				|| (((currentDirection==2) && (FloorRequestsInUp[currentFloor]>0)) 
				||((currentDirection==1) && (FloorRequestsInDown[currentFloor]>0)))){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		openDoors = false;
		String message = "E"+elevatorId+" on F"+currentFloor+" closes";
		myLogger.log(message);
	}

	@Override
	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor){
		if (currentDirection==1){
			String message = "E"+elevatorId+" moves down to F"+floor;
			myLogger.log(message);
		
		}
		else if(currentDirection == 2){ 
			String message = "E"+elevatorId+" moves up to F"+floor;
			myLogger.log(message);
		
		}
		else	System.out.println("E" + elevatorId + " is idle on F" +  floor + ".");
		currentFloor = floor;
	}


	@Override
	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 */

	/* Enter the elevator */
	public synchronized boolean Enter(int rider,int floor, int direction){
		//System.out.println("it should start waiting "+totalRequests);
		while((FloorRequestsOut[currentFloor]>0) || (!openDoors) || 
				(floor!=currentFloor) || (direction!=currentDirection)){ //add something to make sure the elevator is on the right floor
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println("say something");
		if (occupancy==maxOccupancyThreshold){
			if(currentDirection==1) { 
				totalRequests=totalRequests-FloorRequestsInDown[currentFloor];
				FloorRequestsInDown[currentFloor]=0;
			}
			if(currentDirection==2) { 
				totalRequests=totalRequests-FloorRequestsInUp[currentFloor];
				FloorRequestsInUp[currentFloor]=0;
			}
			System.out.println("R"+rider+" tries to get into E"+elevatorId+" but it is full" );
			notifyAll();
			return false;
		}
		String message = "R"+rider+" enters E"+elevatorId+" on F"+floor;
		myLogger.log(message);
		if(direction==1){
			FloorRequestsInDown[currentFloor]--;
			totalRequests--;
			if(FloorRequestsInDown[currentFloor]==0) notifyAll();
			occupancy++;
		}
		else{
			FloorRequestsInUp[currentFloor]--;
			totalRequests--;
			if(FloorRequestsInUp[currentFloor]==0) notifyAll();
			occupancy++;
		}
		return true;
	}

	@Override
	/* Exit the elevator */
	public synchronized void Exit(int rider,int floor){
		//maybe check if it is on the right floor
		//System.out.println("should wait to exit"+openDoors+" "+floor);
		while((!openDoors)||(floor!=currentFloor)){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String message = "R"+rider+" exits E"+elevatorId+" on F"+floor;
		myLogger.log(message);
		FloorRequestsOut[currentFloor]--;
		totalRequests--;
		if(FloorRequestsOut[currentFloor]==0) notifyAll();
		occupancy--;
	}

	@Override
	/* Request a destination floor once you enter */
	public synchronized void RequestFloor(int floor, int rider){
		String message = "R"+rider+" pushes E"+elevatorId+"B"+floor;
		myLogger.log(message);
		if(currentDirection == 0){
			if(currentFloor < floor){
				currentDirection = 2;
			}
			else{
				currentDirection = 1;
			}
		}
		FloorRequestsOut[floor]++;
		totalRequests++;
		assert totalRequests>0;
		//System.out.println("we have to satisfy "+totalRequests);
		notifyAll();
	}

	private boolean makeIdle(){
		return (totalRequests == 0);
	}

	/* Request a source floor while calling elevator */
	public synchronized void RequestFloorIn(int floor,boolean direction){
		if(direction)
			FloorRequestsInUp[floor]++;
		else
			FloorRequestsInDown[floor]++;
		totalRequests++;
		notifyAll();
		
	}

	/* Other methods as needed goes here */
	public int getCurrentFloor(){
		return currentFloor;
	}
	public synchronized void idle(){
		while(totalRequests==0||currentDirection==0){
			System.out.println("The elevator is idle");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	}

	@Override
	//we do a circular scan
	public void run() {
		for(int pula=1;pula<1000;pula++){
		//while(true){
				System.out.println("Pula e "+ pula);
				idle();
				//System.out.println("wtf");
				if(currentDirection == 1){//going down
					//System.out.println("error");
					for(int i = currentFloor ; i > 0; i--){
						if(i!=currentFloor) VisitFloor(i);
						if(FloorRequestsInDown[i]+FloorRequestsOut[i]>0){
							OpenDoors();
							ClosedDoors();
						}	
						if(makeIdle()){
							currentDirection = 0;
							System.out.println("Entering idle.");
							break;
						}
						if(i == 1){
							currentDirection = 2; //make the elevator travel up
						}
					}
				}
				else if(currentDirection == 2){//going up
					//System.out.println("error");
					for(int i = currentFloor ; i <= numFloors; i++){
						if(i!=currentFloor) VisitFloor(i);
						if(FloorRequestsInUp[i]+FloorRequestsOut[i]>0){
							OpenDoors();
							ClosedDoors();
						}	
						if(makeIdle()){
							currentDirection = 0;
							System.out.println("Entering idle.");
							break;
						}
						if(i == numFloors){
							currentDirection = 1; //make the elevator travel up
						}
					}
				
			}
		}
	}	
}

