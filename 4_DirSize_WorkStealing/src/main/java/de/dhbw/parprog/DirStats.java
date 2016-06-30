package de.dhbw.parprog;

public class DirStats {
	int fileCount;
	long totalSize;
	
	public DirStats() {
		fileCount = 0;
		totalSize = 0;
	}

	public DirStats(int fileCount, long totalSize) {
		this.fileCount = fileCount;
		this.totalSize = totalSize;
	}
	
	@Override
	public String toString() {
		return "DirStats(fileCount=" + fileCount + ", totalSize=" + totalSize + ")";
	}
}
