
public class Building extends AbstractBuilding{

	Elevator[] myElevators;
	public Building(int numFloors, int numElevators, Elevator[] elevators) {
		super(numFloors, numElevators);
		myElevators = elevators;
	}

	@Override
	public Elevator CallUp(int fromFloor) {
		int min = Integer.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < numElevators; i++){
			if(myElevators[i].getCurrentFloor()-fromFloor < min){
				index = i;
				min = myElevators[i].getCurrentFloor()-fromFloor;
			}
		}
		myElevators[index].RequestFloor(fromFloor);
		return myElevators[index];
	}

	@Override
	public Elevator CallDown(int fromFloor) {
		int min = Integer.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < numElevators; i++){
			if(myElevators[i].getCurrentFloor()-fromFloor < min){
				index = i;
				min = myElevators[i].getCurrentFloor()-fromFloor;
			}
		}
		myElevators[index].RequestFloor(fromFloor);
		return myElevators[index];
	}

}
