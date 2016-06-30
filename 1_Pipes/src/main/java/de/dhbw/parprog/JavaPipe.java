package de.dhbw.parprog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.dhbw.parprog.processemu.Pipe;
import de.dhbw.parprog.processemu.ProcessEmu;

public class JavaPipe {

	public static void main(String[] args) throws IOException {

		// Variable for total Result
		int overallResult = 0;
		// Number of forked processes to start
		int numberOfForks = 1000;
		// List for holding the pipes corresponding to each forked process
		ArrayList<Pipe> pipes = new ArrayList<Pipe>();

		System.out.println("Kicking off " + numberOfForks + " child processes.");

		// Create pipes and spawn fork processes (performing the generic
		// calculation)
		for (int i = 0; i < numberOfForks; ++i) {
			pipes.add(new Pipe());
			ProcessEmu.fork(pipes.get(i), new CalcTask());
		}

		// Read results from pipes, calculate overall result
		for (int i = 0; i < numberOfForks; ++i) {
			BufferedReader in = new BufferedReader(new InputStreamReader(pipes.get(i).getIn()));
			int result = Integer.parseInt(in.readLine());

			System.out.println("[Fork #" + i + "] Result: " + result);
			overallResult += result;
		}

		System.out.println("Total result of all calculations: " + overallResult);

	}
}
