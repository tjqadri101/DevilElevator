
public class EventBarrier extends AbstractEventBarrier {
	private int numWaiters;
	private boolean event; 
	
	public EventBarrier() {
	}
	/**
	 * Arrive at the barrier and wait until an event is signaled. Return
 	 * immediately if already in the signaled state.
 	 */
	public synchronized void arrive(){
		numWaiters++;
		System.out.println("waiting for barrier");
		while (!event)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		System.out.println("started doing work");
	}	

	/**
	 * Signal the event and block until all threads that wait for this
 	 * event have responded. The EventBarrier returns to an unsignaled state
 	 * before raise() returns.
 	 */	
	public synchronized void raise(){
		event=true;
		System.out.println("barrier up");
		notifyAll();
		while(numWaiters>0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		event=false;
		System.out.println("barrier down");
	}
	
	
	/**
	 * Indicate that the calling thread has finished responding to a
 	 * signaled event, and block until all other threads that wait for 
 	 * this event have also responded.
	 * @throws  
 	 */
	public synchronized void complete() {
		System.out.println("job completed");
		numWaiters--;
		assert numWaiters > -1;
		if(numWaiters==0) notifyAll();
		while(numWaiters>0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("all jobs completed");
	}

	/**
	 * Return a count of threads that are waiting for the event or that
 	 * have not responded yet.
 	 */
	public synchronized int waiters(){
		return numWaiters;
	}	
}

