import java.util.NoSuchElementException;
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
	
	public int getSize(){
		return workQueue.size();
	}
	
	/** remove object from queue, return null if queue empty */
	public String getWork(){
		try{
			return workQueue.remove();
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	
	public String toString(){
		String dirs = null;
		
		for(int i=0;i<workQueue.size();i++)
			dirs = dirs + "\n" + workQueue.remove();
		
		return dirs;
	
	}
}
