package de.dhbw.parprog;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Calculations actor. Performs complex calculation and returns a result when
 * done.
 * 
 * @author maik
 *
 */
public class CalcActor extends AbstractActor {

	// Create new Instance of this actor
	public static Props getProps() {
		return Props.create(CalcActor.class, () -> new CalcActor());
	}

	// Constructor. Defines the initial (and in this case, entire) behavior of
	// this actor.
	public CalcActor() {
		receive(ReceiveBuilder

		// Behavior for messages containing Calculation objects
				.match(Calculation.class, calcObj -> {
					// Wait 1s, calculate the result and return the Calculation
					// object
					// to sender
					Thread.sleep(1000);
					CalcResult calcResult = new CalcResult(calcObj.getFactor() * 42);
					sender().tell(calcResult, self());

					})
				//In case of different message types, do nothing
				.matchAny(unhandledObject -> {

				})

				.build()

		);

	}

}
