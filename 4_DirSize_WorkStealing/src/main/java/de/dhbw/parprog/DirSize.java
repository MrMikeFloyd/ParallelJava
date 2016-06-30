package de.dhbw.parprog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class DirSize {

	DirStats dirStats = new DirStats();
	ForkJoinPool forkJoinPool;

	public DirStats dirStats(File dir) throws IOException {

		// Create a fork join pool (enables work-stealing) and invoke initial
		// worker task
		forkJoinPool = new ForkJoinPool();
		DirStats result = forkJoinPool.invoke(new DirJob(dir));

		return result;

	}

	/**
	 * Worker job class <br>
	 * <p>
	 * Analyzes directory structure and performs file-related calculations.
	 * </p>
	 * <p>
	 * - Adds file information, if regular file is encountered<br>
	 * - Enters recursion and forks another worker task, if subdirectory is
	 * encountered.
	 * </p>
	 * 
	 * @author Maik, Simon
	 *
	 */
	public class DirJob extends RecursiveTask<DirStats> {

		private static final long serialVersionUID = 1L;
		private File _dir;
		private DirStats _subDirStat;
		private ArrayList<ForkJoinTask<DirStats>> _forkedDirJobs = new ArrayList<ForkJoinTask<DirStats>>();

		/**
		 * Constructor. Takes directory as input parameter.
		 * 
		 * @param dir
		 */
		public DirJob(File dir) {
			this._dir = dir;
			this._subDirStat = new DirStats();
		}

		/**
		 * Performs directory analysis. Forks further tasks if subdirectory is
		 * encountered.
		 */
		@Override
		protected DirStats compute() {

			// Get directory contents
			File[] files = _dir.listFiles();
			List<File> fileList = Arrays.asList(files);

			// Iterate over list of files
			for (File file : fileList) {
				if (file.isFile()) {
					// If current object is regular file, retrieve relevant
					// information
					_subDirStat.fileCount++;
					_subDirStat.totalSize += file.length();
				} else if (file.isDirectory()) {
					// We are dealing with a directory - enter recursion and
					// fork another task
					_forkedDirJobs.add(new DirJob(file).fork());

				} else {
					System.out.println("File '" + file.getAbsolutePath() + "' is neither regular file nor directory - ignoring.");
				}

			}

			try {
				for (ForkJoinTask<DirStats> forkedTask : _forkedDirJobs) {
					// Consolidate work - sum up data from all forked tasks
					DirStats forkedDirStats = forkedTask.join();
					_subDirStat.fileCount += forkedDirStats.fileCount;
					_subDirStat.totalSize += forkedDirStats.totalSize;
				}

			} catch (Exception e) {
				System.out.println("Directory analysis interrupted or error encountered.");
				e.printStackTrace();
			}

			return _subDirStat;
		}

	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Benötigter Parameter: Startverzeichnis");
			System.exit(1);
		}
		File startDir = new File(args[0]);
		if (!startDir.isDirectory()) {
			System.out.println("Dies ist kein Verzeichnis!");
			System.exit(1);
		}

		DirSize test = new DirSize();
		DirStats result = test.dirStats(startDir);
		System.out.println(result.fileCount + " Dateien, " + result.totalSize + " Bytes.");
	}
}
