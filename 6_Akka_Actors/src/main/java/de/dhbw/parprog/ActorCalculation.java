package de.dhbw.parprog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Terminated;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;

public class ActorCalculation {
	// Wait duration for calculation task
	static final long waitTime = 4000;

	public static void main(String[] args) {
		System.out.println("Important calculation - with actors");
		ActorSystem system = ActorSystem.create("actors");

		// Create Round Robin Pool
		ActorRef actorRouterRef = system.actorOf(new RoundRobinPool(5).props(CalcActor.getProps()), "calcActor");

		// Enqueue calculations via actor router; add expected results to list
		// of futures
		ArrayList<Future<Object>> results = new ArrayList<Future<Object>>();
		for (int i = 0; i < 10; i++) {

			results.add(Patterns.ask(actorRouterRef, new Calculation(i + 1), waitTime));
		}

		// Variable for holding sum of all calculations
		double finalResult = 0;

		// Iterate over list of results. CalcResult objects are being expected
		// and summed up.
		// If no CalcResult object is returned, ignore the object.
		for (Future<Object> resultReply : results) {
			try {
				// Block during queue read, wait for up to 5s. Check, if we
				// really have a CalcResult object.
				Object result = Await.result(resultReply, Duration.create(5, TimeUnit.SECONDS));
				if (result instanceof CalcResult) {
					// Before summing up, perform cast to CalcResult
					finalResult += ((CalcResult) result).getResult();
				} else {
					// Object is of unknown type - ignore it
					System.out.println("Result object is of unknown type '" + result.getClass() + "' - ignoring.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// We have read all result messages - print the final result
		System.out.println("Calculations finished.\nThe sum of all results is: " + finalResult);

		// Done - shut down the actor system
		Future<Terminated> theEnd = system.terminate();
		try {
			Await.ready(theEnd, Duration.apply(10, TimeUnit.SECONDS));
		} catch (InterruptedException | TimeoutException e) {
			e.printStackTrace();
		}

	}
}
