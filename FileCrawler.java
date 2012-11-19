import java.io.File;


public class FileCrawler {
	
	public static void main(String args[]){
		String pattern = null;
		String directory = null;
		int noThreads = 0;
		WorkQueue workQueue = new WorkQueue();
		
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
			
			/** STATIC VERSION
			 * find matching file names in each directory in workQueue
			 */
			File srcDir;
			for(int i=0;i<workQueue.getSize();i++){
				/** for each directory */
				String srcDirStr = workQueue.getWork();
				srcDir = new File(srcDirStr);
				String files[] = srcDir.list();
				for(String curFile:files){
					File testDir = new File(srcDirStr + "/" + curFile);
					if(!testDir.isDirectory()){
						/** for each file in dir */
						if(Regex.match(curFile, pattern))
							System.out.println(curFile);
					}
				}
			}
			
			//System.out.println(workQueue);
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
