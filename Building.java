
public class Building extends AbstractBuilding{

	private Elevator[] myElevators;
	private MyLogger myLogger;
	public Building(int numFloors, int numElevators, Elevator[] elevators, MyLogger logger) {
		super(numFloors, numElevators);
		myElevators = elevators;
		myLogger = logger;
	}

	@Override
	//We do a circular scan
	public Elevator CallUp(int fromFloor, int riderId){
		int distance;
		boolean directionForIdle = false; //false go down, true go up
		int min = Integer.MAX_VALUE;
		int index = 0;
		
		for(int i = 0; i < numElevators; i++){
			if(myElevators[i].currentDirection == 0){//currently idle
				if(myElevators[i].getCurrentFloor() <= fromFloor){
					distance = myElevators[i].getCurrentFloor()-fromFloor;
					if(Math.abs(distance) < min){
						index = i;
						min = Math.abs(distance);
						directionForIdle = true;
					}
				}
				else{
					distance = myElevators[i].getCurrentFloor() - 1; //distance from current floor to first floor
					distance += fromFloor - 1; //add distance back up from first floor to fromFloor
					if(distance < min){
						index = i;
						min = Math.abs(distance);
						directionForIdle = false;
					}
				}
			}
			else if(myElevators[i].currentDirection == 1){//going down
				distance = myElevators[i].getCurrentFloor() - 1; //distance from current floor to first floor
				distance += fromFloor - 1; //actual distance elevator must travel
				//to service request
				if(distance < min){
					index = i;
					min = distance;
				}
			}
			else if(myElevators[i].currentDirection == 2){//going up
				if(myElevators[i].getCurrentFloor() <= fromFloor){
					if(Math.abs(myElevators[i].getCurrentFloor()-fromFloor) < min){
						index = i;
						min = Math.abs(myElevators[i].getCurrentFloor()-fromFloor);
					}
				}
				else{//elevators current floor greater than fromFloor
					distance = numFloors - myElevators[i].getCurrentFloor(); //distance from current floor to top floor
					distance += numFloors - 1; //add distance from top floor to bottom floor
					distance += fromFloor - 1; //add distance from first floor to fromFloor
					if(distance < min){
						index = i;
						min = distance;
					}
				}
			}
		}
		//System.out.printf("R%d pushes U%d.\n", riderId, fromFloor);
		//myElevators[index].RequestFloorIn(fromFloor,true);
		if(myElevators[index].currentDirection == 0){
			//System.out.println("debugging");
			if(directionForIdle){
				myElevators[index].currentDirection = 2;
			}
			else{
				myElevators[index].currentDirection = 1;
			}
			//System.out.println("no of requests"+ myElevators[index].totalRequests);
			//System.out.println("direction"+myElevators[index].currentDirection);
		}
		myElevators[index].RequestFloorIn(fromFloor,true);
		String message = "R" + riderId + " pushes U" + fromFloor;
		//System.out.printf("R%d pushes U%d.\n", riderId, fromFloor);
		myLogger.log(message);
		return myElevators[index];
	}

	@Override
	//We do a circular scan
	public Elevator CallDown(int fromFloor, int riderId){
		int distance;
		boolean directionForIdle = false; //false go down, true go up
		int min = Integer.MAX_VALUE;
		int index = 0;
		
		for(int i = 0; i < numElevators; i++){
			if(myElevators[i].currentDirection == 0){//currently idle
				if(myElevators[i].getCurrentFloor() >= fromFloor){
					distance = myElevators[i].getCurrentFloor()-fromFloor;
					if(Math.abs(distance) < min){
						index = i;
						min = Math.abs(distance);
						directionForIdle = false;
					}
				}
				else{
					distance = numFloors - myElevators[i].getCurrentFloor(); //distance from current floor to top floor
					distance += numFloors - fromFloor; //add distance from top floor to fromFloor
					if(distance < min){
						index = i;
						min = Math.abs(distance);
						directionForIdle = true;
					}
				}
			}
			else if(myElevators[i].currentDirection == 1){//going down
				if(myElevators[i].getCurrentFloor() >= fromFloor){
					distance = myElevators[i].getCurrentFloor()-fromFloor;
					if(Math.abs(distance) < min){
						index = i;
						min = Math.abs(distance);
						directionForIdle = false;
					}
				}
				else{
					distance = myElevators[i].getCurrentFloor() - 1; //distance from current floor to first floor
					distance += numFloors - 1; //add distance from first floor to top floor
					distance += numFloors - fromFloor; //add distance from top floor to fromFloor
					//to service request
					if(distance < min){
						index = i;
						min = distance;
					}
				}
			}
			else if(myElevators[i].currentDirection == 2){//going up
				distance = numFloors - myElevators[i].getCurrentFloor(); //distance from current floor to top floor
				distance += numFloors - fromFloor; //add distance from top floor to fromFloor
				if(distance < min){
					index = i;
					min = distance;
				}
			}
		}
		//System.out.printf("R%d pushes D%d.\n", riderId, fromFloor);
		//myElevators[index].RequestFloorIn(fromFloor,false);
		if(myElevators[index].currentDirection == 0){
			//System.out.println("debugging");
			if(directionForIdle){
				myElevators[index].currentDirection = 2;
			}
			else{
				myElevators[index].currentDirection = 1;
			}
			//System.out.println("no of requests"+ myElevators[index].totalRequests);
			//System.out.println("direction"+myElevators[index].currentDirection);
		}
		//System.out.printf("R%d pushes D%d.\n", riderId, fromFloor);
		myElevators[index].RequestFloorIn(fromFloor,false);
		String message = "R" + riderId + " pushes D" + fromFloor;
		myLogger.log(message);
		return myElevators[index];
	}

}
