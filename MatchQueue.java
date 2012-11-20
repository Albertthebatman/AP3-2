import java.util.concurrent.PriorityBlockingQueue;

public class MatchQueue {
	private PriorityBlockingQueue<String> matchQueue;
	
	public MatchQueue(){
		matchQueue = new PriorityBlockingQueue<String>();
	}
	
	public synchronized void add(String file){
		matchQueue.add(file);
	}
	
	public void display(){
		while(!matchQueue.isEmpty()){
			System.out.println(matchQueue.remove());
		}
	}
}
