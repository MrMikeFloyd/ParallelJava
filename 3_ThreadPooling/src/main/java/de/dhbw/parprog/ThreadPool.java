package de.dhbw.parprog;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {

	final int numberOfThreads = 5;
	final int numberOfCalculations = 10;

	public int doCalculation() {

		// Create set for storing intermediary results
		Set<Future<Integer>> results = new HashSet<Future<Integer>>(numberOfCalculations);
		// Create the thread pool
		ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);

		// Create 10 calculation tasks and submit them to the thread pool; add
		// individual results to the results set
		for (int i = 0; i < numberOfCalculations; i++) {
			Future<Integer> currentResult = threadPool.submit(new calcThread());
			results.add(currentResult);
		}

		// Read results from hash set and calculate the overall result
		int sumOfAllResults = 0;
		for (Future<Integer> result : results) {
			try {
				sumOfAllResults += result.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}

		return sumOfAllResults;
	}

	public class calcThread implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			try {
				// Sleep for 1s, then return 42
				Thread.sleep(1000);
				return 42;
			} catch (InterruptedException e) {
				System.out.println("Calculation thread interrupted.");
				return 0;
			}
		}

	}

	public static void main(String[] args) {
		ThreadPool test = new ThreadPool();
		System.out.println("Calculation started");
		int result = 0;
		try {
			result += test.doCalculation();
			System.out.println("Result: " + result);
		} catch (CancellationException e) {
			System.out.println("It seems that tasks have been interrupted, can't calculate overall result.");

		}
	}
}
