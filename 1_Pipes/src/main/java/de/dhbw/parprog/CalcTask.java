package de.dhbw.parprog;

import java.io.PrintStream;

import de.dhbw.parprog.processemu.ProcessWithPipe;

public class CalcTask implements ProcessWithPipe {
	@Override
	public void main(final PrintStream outPipe) {
		try {
			// Sleep for 1s, then send "42" to pipe
			Thread.sleep(1000);
			outPipe.println("42");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
