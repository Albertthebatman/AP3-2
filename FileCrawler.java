import java.io.File;


public class FileCrawler {
	
	public static void main(String args[]){
		String pattern = null;
		String directory = null;
		int noThreads = 0;
		WorkQueue workQueue = new WorkQueue();		//Storage for work queue
		MatchQueue matchQueue = new MatchQueue();	//Storage for match queue
		
		/** assign program parameters */
		if(args.length < 1 || args.length > 2){
			System.out.println("Correct usage: java FileCrawler pattern [directory]");
		} else {
			/** correct number of args */
			
			/** convert pattern to regex */
			pattern = Regex.cvtPattern(args[0]);
			
			/** directory to search in (optional)
			 * defualt to current if not set */
			if(args.length==2)
				directory = args[1];
			else
				directory = ".";
		
			
			/** no of threads
			 * if no environment variable set, default to 2 */
			if(System.getenv("CRAWLER_THREADS") == null)
				noThreads = 2;
			else
				noThreads = Integer.parseInt(System.getenv("CRAWLER_THREADS"));
			
			/** Loop through adding each directory to work */
			findDirectories(directory,workQueue);
			
			
			/** Start threads 
			Thread[] threads = new Thread[noThreads];
			for(int i=0;i<noThreads;i++){
				threads[i] = new Thread(new WorkerThread(workQueue,matchQueue,pattern));
				threads[i].start();
			}
			
			/** When threads finish, join together 
			for(int i=0;i<noThreads;i++){
				try{
					threads[i].join();
				} catch(InterruptedException e){
					
				}
			}*/
			
			/** Single threaded implementation */
			for(int i=0;i<workQueue.getSize();i++){
				String currentWork = workQueue.getWork();
				File srcDir = new File(currentWork);
				String files[] = srcDir.list();
				for(String file:files){
					File testFile = new File(file);
						if(!testFile.isDirectory()){
						/** file is not a directory, regex match */
						if(Regex.match(file, pattern))
							matchQueue.add(currentWork + "/" + file);
						}
				}
			}
			
			/** Display matchQueue */
			matchQueue.display();

		}
	}
	
	/** finds all directories starting at currentDirectory and adds to work queue */
	public static void findDirectories(String currentDirectory,WorkQueue workQueue){
		try{
			File currentDir = new File(currentDirectory);
			
			if(currentDir.isDirectory()){
				/** Directory is valid */
				
				/** Add directory to work queue */
				workQueue.add(currentDirectory);
				
				/** generate file listing for current dir */
				String files[] = currentDir.list();
				
				for(String nextFile:files){
					/** skip over current and previous directory pointers */
					if(nextFile.compareTo(".")==0)
						continue;
					if(nextFile.compareTo("..")==0)
						continue;
					
					/** Repeat for every other file listed in currentDir */
					findDirectories(currentDirectory + "/" + nextFile,workQueue);
				}
			}
		} catch(Exception e){
			System.out.println("Error - Could not process directory " + currentDirectory);
		}
	}
}
