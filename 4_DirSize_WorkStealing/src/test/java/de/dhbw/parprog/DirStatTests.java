package de.dhbw.parprog;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DirStatTests {
	@Test
	public void correctStatsInTestdir() throws IOException {
		File testDir = new File("src/test/resources/testdir");
		assertThat(testDir.isDirectory()).isTrue();

		DirSize test = new DirSize();
		DirStats stats = test.dirStats(testDir);
		assertThat(stats.fileCount).isEqualTo(38);
		assertThat(stats.totalSize).isEqualTo(3230);
	}
}
