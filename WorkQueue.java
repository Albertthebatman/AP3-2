import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkQueue {
	private LinkedBlockingQueue<String> workQueue;
	
	public WorkQueue(){
		workQueue = new LinkedBlockingQueue<String>();
	}
	
	public LinkedBlockingQueue<String> getQueue(){
		return workQueue;
	}
	
	/** add directory to work queue, notify any sleeping threads */
	public synchronized void add(String dir){
		workQueue.offer(dir);
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
		
			return workQueue.poll();
	}
	
	public String toString(){
		String dirs = null;
		
		while(workQueue.peek()!=null){
			dirs = dirs + "\n" + workQueue.poll();
			
		}
		
		return dirs;
	
	}
}
