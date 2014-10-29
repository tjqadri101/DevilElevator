
public class TestEventBarrier  {
	EventBarrier barrier = new EventBarrier();
	public class Producer implements Runnable{
		public Producer(){	
		}
		public void run(){
			while(true){
				
				barrier.raise();
				
			}	
		}
	}
	public class Consumer implements Runnable{
		public Consumer(){
		}
		public void run(){
			while(true){
				barrier.arrive();
				int s=0;
				for(int i=1;i<3000000;i++){
					s=i;
				}
				System.out.println(s);
				barrier.complete();
			}		
		}
	}
	public static void main(String args[]){
		TestEventBarrier test = new TestEventBarrier();
		Producer p = test.new Producer();
		Consumer c = test.new Consumer();
		Thread t = new Thread(p);
		for(int i=1;i<10;i++){
			Thread u = new Thread(c);
			u.start();
		}
		t.start();
		
	}
}
