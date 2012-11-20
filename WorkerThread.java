import java.io.File;


public class WorkerThread implements Runnable {
	private WorkQueue queue;
	private String pattern;
	private MatchQueue matchQueue;
	
	public WorkerThread(WorkQueue queue, MatchQueue matchQueue, String pattern){
		this.queue = queue;
		this.matchQueue = matchQueue;
		this.pattern = pattern;
	}
	
	public void run(){
		/** worker thread code */
		String currentWork = queue.getWork();
		File srcDir = new File(currentWork);
		String[] files = srcDir.list();
		for(String file:files){
			File testFile = new File(file);
			if(!testFile.isDirectory()){
				/** file is not a directory, regex match */
				if(Regex.match(file, pattern))
					matchQueue.add(file);
			}
		}
	}
}
