
public class TestEventBarrier  {
	EventBarrier barrier = new EventBarrier();
	public class Producer implements Runnable{
		public Producer(){	
		}
		public void run(){
			for(int j=1;j<2;j++){   //how many times the barrier is raised
				
				barrier.raise();  
				
			}	
		}
	}
	public class Consumer implements Runnable{
		public Consumer(){
		}
		public void run(){
			for(int j=1;j<2;j++){   //how many times a thread calls arrive
				barrier.arrive();
				int s=0;
				for(int i=1;i<3000000;i++){   //useless work the consumer thread does
					s=i;
				}
				System.out.println(s);
				barrier.complete();
			}		
		}
	}
	public static void test(){
		TestEventBarrier test = new TestEventBarrier();
		Producer p = test.new Producer();
		Consumer c = test.new Consumer();
		Thread t = new Thread(p);
		for(int i=1;i<10;i++){        //you can change the limits of the for for more threads
			Thread u = new Thread(c);
			u.start();
		}
		t.start();
		
	}
}
