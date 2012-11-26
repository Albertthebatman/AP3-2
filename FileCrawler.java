import java.io.File;


public class FileCrawler {
	static WorkQueue workQueue = new WorkQueue();

	public static void main(String args[]){
		String pattern = null;
		String directory = null;
		int noThreads = 0;
		//WorkQueue workQueue = new WorkQueue();		//Storage for work queue
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
			findDirectories(directory);


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
			while(workQueue.getSize()>0){
				String currentWork = workQueue.getWork();
				File srcDir = null;

				try{
					srcDir = new File(currentWork);
				} catch(Exception e){
					System.out.println("Cannot access");
				}

				String files[] = srcDir.list();
				File testFile = null;

				for(String file:files){
					try{
						testFile = new File(currentWork + "/" + file);
					} catch(Exception e){
						System.out.println("Cannot access");
					}
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
	public static void findDirectories(String currentDirectory){
		try {
			File file = new File(currentDirectory); // create a File object
			if (file.isDirectory()) { // a directory - could be symlink
				//System.out.println("## - " + currentDirectory + " is directory");
				String entries[] = file.list();
				if (entries != null) { // not a symlink
					//System.out.println("## - " + currentDirectory + " is not symlink");
					if(!workQueue.add(currentDirectory))
						System.out.println("FUCK MY LIFE");
					for (String entry : entries ) {
						if (entry.compareTo(".") == 0)
							continue;
						if (entry.compareTo("..") == 0)
							continue;
						findDirectories(currentDirectory+"/"+entry);
					}
				}
			} else {
				//buggggyfix
				//System.out.println("## - " + currentDirectory + " is not a dir.");
			}
		} catch (Exception e) {
			System.err.println("Error processing "+currentDirectory+": "+e);
		}
	}
}
