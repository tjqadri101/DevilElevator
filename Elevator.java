
public class Elevator extends AbstractElevator implements Runnable{

	public int currentDirection; //0 idle, 1 down, 2 up
	private boolean openDoors;
	//private int numPeopleIn;
	//private int numPeopleOut;
	private int occupancy;
	private int[] FloorRequestsInUp = new int[numFloors+1]; //no floor at index 0
	private int[] FloorRequestsInDown = new int[numFloors+1];
	private int[] FloorRequestsOut = new int[numFloors+1];//no floor at index 0
	private int totalRequests;
	private int currentFloor;
	/**
	 * Other variables/data structures as needed goes here 
	 */

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		//Initialize the elevator to start at the first floor
		VisitFloor(1);
		currentDirection = 0; //initialize the elevator to be initially idle
	}

	@Override
	/**
	 * Elevator control interface: invoked by Elevator thread.
	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors(){
		openDoors = true;
		System.out.println("E"+elevatorId+" on F"+currentFloor+" opens");
		notifyAll();
	}

	@Override
	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 */
	public synchronized void ClosedDoors(){
		assert openDoors;
		//while(((currentDirection==2) && (FloorRequestsInUp[currentFloor])>0))||(FloorRequestsOut[currentFloor]>0)&&(occupancy<maxOccupancyThreshold)){
		while((occupancy<maxOccupancyThreshold)&&((FloorRequestsOut[currentFloor]>0)||(((currentDirection==2)&&(FloorRequestsInUp[currentFloor]>0))||((currentDirection==1)&&(FloorRequestsInDown[currentFloor]>0))))){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		openDoors = false;
		System.out.println("E"+elevatorId+" on F"+currentFloor+" closes");
	}

	@Override
	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor){
		if (currentDirection==1) System.out.println("E"+elevatorId+" moves down to F"+floor);
		else System.out.println("E"+elevatorId+" moves up to F"+floor);
		currentFloor = floor;
	}


	@Override
	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 */

	/* Enter the elevator */
	public synchronized boolean Enter(int rider,int floor, int direction){
		while((FloorRequestsOut[currentFloor]>0)||(!openDoors)||(floor!=currentFloor)||(direction!=currentDirection)){ //add something to make sure the elevator is on the right floor
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (occupancy==maxOccupancyThreshold){
			notifyAll();
			System.out.println("R"+rider+" tries to get into E"+elevatorId+"but it is full" );
			return false;
		}
		System.out.println("R"+rider+" enters E"+elevatorId+" on F"+floor);
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
		while((!openDoors)||(floor!=currentFloor)){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("R"+rider+" exits E"+elevatorId+" on F"+floor);
		FloorRequestsOut[currentFloor]--;
		totalRequests--;
		if(FloorRequestsOut[currentFloor]==0) notifyAll();
		occupancy--;
	}

	@Override
	/* Request a destination floor once you enter */
	public synchronized void RequestFloor(int floor, int rider){
		System.out.println("R"+rider+" pushes E"+elevatorId+"B"+floor);
		FloorRequestsOut[floor]++;
		totalRequests++;
	}

	private boolean isIdle(){
		return totalRequests == 0;
	}

	/* Request a source floor while calling elevator */
	public void RequestFloorIn(int floor,boolean direction){
		if(direction)
			FloorRequestsInUp[floor]++;
		else
			FloorRequestsInDown[floor]++;
		totalRequests++;
	}

	/* Other methods as needed goes here */
	public int getCurrentFloor(){
		return currentFloor;
	}

	@Override
	//we do a circular scan
	public void run() {
		while(true){
			if(!isIdle()){
				if(currentDirection == 1){//going down
					for(int i = currentFloor -1; i > 0; i--){
						VisitFloor(i);
						if(FloorRequestsInDown[i]+FloorRequestsOut[i]>0){
							OpenDoors();
							ClosedDoors();
						}	
						if(isIdle()){
							currentDirection = 0;
							break;
						}
						if(i == 1){
							currentDirection = 2; //make the elevator travel up
						}
					}
				}
				else if(currentDirection == 2){//going up
					for(int i = currentFloor+1; i <= numFloors; i++){
						VisitFloor(i);
						if(FloorRequestsInUp[i]+FloorRequestsOut[i]>0){
							OpenDoors();
							ClosedDoors();
						}	
						if(isIdle()){
							currentDirection = 0;
							break;
						}
						if(i == numFloors){
							currentDirection = 1; //make the elevator travel up
						}
					}
				}
			}
			else{
				System.out.println("Elevator is idle");
			}
		}
	}	
}

