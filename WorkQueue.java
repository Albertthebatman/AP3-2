import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkQueue {
	private ConcurrentLinkedQueue<String> workQueue;
	
	public WorkQueue(){
		workQueue = new ConcurrentLinkedQueue<String>();
	}
	
	public ConcurrentLinkedQueue<String> getQueue(){
		return workQueue;
	}
	
	public void add(String dir){
		workQueue.add(dir);
	}
}
