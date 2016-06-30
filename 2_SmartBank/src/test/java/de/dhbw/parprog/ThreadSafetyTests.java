package de.dhbw.parprog;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ThreadSafetyTests {

	/**
	 * Embedded MoneyTransfer class for running money transactions in multiple
	 * threads.
	 */
	class MoneyTransfer implements Runnable {
		Account fromAccount, toAccount;
		long amount;
		int count;

		/**
		 * Constructor.
		 * 
		 * @param fromAccount
		 * @param toAccount
		 * @param amount
		 * @param count
		 */
		MoneyTransfer(Account fromAccount, Account toAccount, long amount, int count) {
			this.fromAccount = fromAccount;
			this.toAccount = toAccount;
			this.amount = amount;
			this.count = count;
		}

		/**
		 * Transfer method. Delays transfers by 1ms to provoke race conditions
		 * 
		 * @param fromAccount
		 * @param toAccount
		 * @param amount
		 */
		void transfer(Account fromAccount, Account toAccount, long amount) {
			bank.transfer(fromAccount, toAccount, amount);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Thread execution interrupted during transfer operation.");
			}

		}

		/**
		 * Run method. Performs n number of transfers.
		 */
		@Override
		public void run() {
			for (int i = 0; i < count; i++) {
				transfer(fromAccount, toAccount, amount);
			}
		}
	}

	Bank bank;
	Account a1;
	Account a2;
	static final long INITIAL_BALANCE = 1000;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setupTest() {
		bank = new Bank();
		a1 = bank.createAccount();
		a2 = bank.createAccount();

	}

	@Test
	public void canPerformThreadSafeFinancialTransactions() {

		bank.deposit(a1, INITIAL_BALANCE);
		bank.deposit(a2, INITIAL_BALANCE);
		System.out.println("Initial balance of test accounts before test:\n\tAccount 1: " + a1.getAmount() + "\n\tAccount 2: "
				+ a2.getAmount());

		try {
			Thread t1 = new Thread(new MoneyTransfer(a1, a2, 10, 5000));
			Thread t2 = new Thread(new MoneyTransfer(a2, a1, 10, 5000));

			t1.start();
			t2.start();

			t1.join();
			t2.join();

		} catch (Exception e) {
			System.out.println("Thread execution interrupted during MoneyTransfer test.");
		}

		System.out.println("Final balance of test accounts after test:\n\tAccount 1: " + a1.getAmount() + "\n\tAccount 2: "
				+ a2.getAmount());

		assertThat(a1.getAmount()).isEqualTo(INITIAL_BALANCE);
		assertThat(a2.getAmount()).isEqualTo(INITIAL_BALANCE);
	}

}
