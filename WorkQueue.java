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
	
	/** add directory to work queue, notify any sleeping threads */
	public synchronized void add(String dir){
		workQueue.add(dir);
		notifyAll();
	}
	
	public int getSize(){
		return workQueue.size();
	}
	
	/** remove object from queue, sleep thread if no work */
	public synchronized String getWork(){
		if(workQueue.isEmpty())
			try {
				wait();
			} catch (InterruptedException e1) {
				
			}
		
			return workQueue.remove();
	}
	
	public String toString(){
		String dirs = null;
		
		for(int i=0;i<workQueue.size();i++)
			dirs = dirs + "\n" + workQueue.remove();
		
		return dirs;
	
	}
}
