
public class Elevator {

	protected int numFloors; 
	protected int elevatorId;
	protected int maxOccupancyThreshold;
	private boolean openDoors;
	//private int numPeopleIn;
	//private int numPeopleOut;
	private int occupancy;
	private int[] FloorRequestsIn = new int[numFloors];
	private int[] FloorRequestsOut = new int[numFloors];
	private int currentFloor;
	/**
	 * Other variables/data structures as needed goes here 
	 */

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		this.numFloors = numFloors;
		this.elevatorId = elevatorId;
		this.maxOccupancyThreshold = maxOccupancyThreshold;
	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
 	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors(){
		openDoors=true;
		notifyAll();
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
 	 */
	public synchronized void ClosedDoors(){
		assert openDoors;
		while((FloorRequestsIn[currentFloor]+FloorRequestsOut[currentFloor]>0)&&(occupancy<maxOccupancyThreshold)){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		openDoors=false;
	}

	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor){
		currentFloor=floor;
	}


	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
  	 */

	/* Enter the elevator */
	public synchronized boolean Enter(){
		while((FloorRequestsOut[currentFloor]>0)||(!openDoors)){ //add something to make sure the elevator is on the right floor
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (occupancy==maxOccupancyThreshold){
			notifyAll();
			return false;
		}
		FloorRequestsIn[currentFloor]--;
		if(FloorRequestsIn[currentFloor]==0) notifyAll();
		occupancy++;
		return true;
	}
	
	/* Exit the elevator */
	public synchronized void Exit(){
		//maybe check if it is on the right floor
		while(!openDoors){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FloorRequestsOut[currentFloor]--;
		if(FloorRequestsOut[currentFloor]==0) notifyAll();
		occupancy--;
	}

	/* Request a destination floor once you enter */
 	public synchronized void RequestFloor(int floor){
 		FloorRequestsOut[floor]++;
 	}
	
	/* Other methods as needed goes here */
}

