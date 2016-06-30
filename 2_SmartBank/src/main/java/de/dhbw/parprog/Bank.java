package de.dhbw.parprog;

public class Bank {
	/**
	 * Erzeugt ein neues Konto mit initialem Kontostand 0.
	 * 
	 * @return das neue Konto
	 */
	public Account createAccount() {
		return new Account();
	}

	/**
	 * Ruft den aktuellen Kontostand ab
	 * 
	 * @param account
	 *            Konto, dessen Kontostand bestimmt werden soll
	 * @return der aktuelle Kontostand
	 * @throws IllegalArgumentException
	 *             bei ungültigen Parametern
	 */
	public long getBalance(Account account) throws IllegalArgumentException {
		return account.getAmount();
	}

	/**
	 * Einzahlen eines bestimmten Betrags
	 * 
	 * @param account
	 *            das Konto, auf den der Betrag eingezahlt werden soll
	 * @param amount
	 *            der Betrag (muß >=0 sein)
	 * @throws IllegalArgumentException
	 *             bei ungültigen Parametern
	 * @throws IllegalAccountStateException
	 *             falls der Kontostand außerhalb des gültigen Wertebereichs
	 *             geraten würde
	 */
	public void deposit(Account account, long amount) throws IllegalAccountStateException, IllegalArgumentException {
		try {
			account.incrementAmount(amount);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Abheben eines bestimmten Betrags
	 * 
	 * @param account
	 *            das Konto, von dem der Betrag abgehoben werden soll
	 * @param amount
	 *            der Betrag (muß >=0 sein)
	 * @throws IllegalArgumentException
	 *             bei ungültigen Parametern
	 * @throws IllegalAccountStateException
	 *             falls der Kontostand außerhalb des gültigen Wertebereichs
	 *             geraten würde
	 */
	public void withdraw(Account account, long amount) throws IllegalAccountStateException, IllegalArgumentException {
		try {
			account.decrementAmount(amount);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Überweisen eines Betrags von einem Konto auf ein anderes
	 * 
	 * @param fromAccount
	 *            Konto, von dem abgebucht werden soll
	 * @param toAccount
	 *            Konto, auf das gutgeschrieben werden soll
	 * @param amount
	 *            der zu transferierende Betrag (muß >=0 sein)
	 * @throws IllegalArgumentException
	 *             bei ungültigen Parametern
	 * @throws IllegalAccountStateException
	 *             falls einer der Kontostände außerhalb des gültigen
	 *             Wertebereichs geraten würde
	 */
	public void transfer(Account fromAccount, Account toAccount, long amount) throws IllegalAccountStateException, IllegalArgumentException {
		try {
			fromAccount.decrementAmount(amount);
			toAccount.incrementAmount(amount);
		} catch (Exception e) {
			throw e;
		}
	}
}
