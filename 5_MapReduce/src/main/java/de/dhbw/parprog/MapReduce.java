package de.dhbw.parprog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class MapReduce {

	ForkJoinPool forkJoinPool;

	public MapReduce() {
		// Create new ForkJoinPool (4 parallel tasks)
		this.forkJoinPool = new ForkJoinPool(4);
	}

	/**
	 * Wandelt eine Liste von Futures in das Future einer Liste aller Ergebnisse
	 * um
	 *
	 * @param futures
	 * @param <T>
	 * @return
	 */
	public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
		CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
		return allDoneFuture.thenApply(v -> futures.stream().map(future -> future.join()).collect(Collectors.<T> toList()));
	}

	/**
	 * Returns list of people from PersonArchive. When being used in a multi-
	 * threaded environment potentially contains a fraction of the archive, i.e.
	 * lists need to be merged.
	 * 
	 * @return List of people from PersonArchive
	 */
	public List<Person> getPeople() {

		ArrayList<Person> peopleList = new ArrayList<Person>();
		boolean archiveEmpty = false;

		// Unless the personArchive is empty, retrieve as many Person records as
		// possible
		do {

			Person currentPerson = PersonArchive.getPerson();
			if (currentPerson != null) {

				peopleList.add(currentPerson);
			} else {

				archiveEmpty = true;
			}

		} while (!archiveEmpty);

		return peopleList;
	}

	/**
	 * Performs Analysis of Records from PersonArchive.
	 * 
	 * @return CompletableFuture containing the CalcResult object holding
	 *         results from all partial calculations.
	 */
	public CompletableFuture<CalcResult> doAnalysis() {

		// Create list to hold partial lists of Person objects from
		// PersonArchive
		ArrayList<CompletableFuture<List<Person>>> peopleFragments = new ArrayList<CompletableFuture<List<Person>>>();
		for (int i = 0; i < 4; i++) {

			// Retrieve list of people from PersonArchive (subset of entire
			// archive)
			CompletableFuture<List<Person>> peopleFragment = CompletableFuture.supplyAsync(() -> {

				// Retrieve as many People records as possible
					return this.getPeople();
				}, forkJoinPool);

			peopleFragments.add(peopleFragment);
		}

		// Convert list of futures into future of lists
		CompletableFuture<List<List<Person>>> assembledList = sequence(peopleFragments);

		// Merge the lists of people gathered by the worker threads into one
		// list holding all records
		CompletableFuture<List<Person>> assembledPeopleList = assembledList.thenApply(listOfPeopleLists -> {

			// Final list to hold all Person records
				ArrayList<Person> tmpAssembledPeopleList = new ArrayList<Person>();

				for (List<Person> peopleList : listOfPeopleLists) {

					tmpAssembledPeopleList.addAll(peopleList);
				}

				return tmpAssembledPeopleList;
			});

		// Calculate average age across the entire persons archive
		CompletableFuture<Double> avgAge = assembledPeopleList.thenApplyAsync((personArchive) -> {
			double sumOfAges = 0;
			for (Person person : personArchive) {
				sumOfAges += person.alter;
			}

			return sumOfAges / personArchive.size();
		}, forkJoinPool);

		// Determine length of longest last name in persons archive
		CompletableFuture<Integer> maxLen = assembledPeopleList.thenApplyAsync((personArchive) -> {
			Integer maxLength = 0;
			for (Person person : personArchive) {
				if (maxLength < person.name.length()) {
					maxLength = person.name.length();
				}
			}

			return maxLength;
		}, forkJoinPool);

		// Sum up number of males in persons archive
		CompletableFuture<Integer> maleCount = assembledPeopleList.thenApplyAsync((personArchive) -> {

			int numMales = 0;
			for (Person person : personArchive) {
				if (person.male) {
					numMales++;
				}
			}
			return numMales;
		}, forkJoinPool);

		// Return CompletableFuture holding the final result
		return CompletableFuture.supplyAsync(() -> {

			// Create CalcResult object and add all partial results
				return new CalcResult(avgAge.join(), maxLen.join(), maleCount.join());
			}, forkJoinPool);

	}
}
