package de.dhbw.parprog;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {
	public static long LOWER_LIMIT = 0;
	public static long UPPER_LIMIT = 100000;

	private long amount;
	private String name;
	ReadWriteLock lock;
	Lock writeLock;
	Lock readLock;

	/**
	 * Constructor, initializes account with 0
	 */
	public Account() {
		this.name = "";
		this.amount = 0;

		// Create Locks
		this.lock = new ReentrantReadWriteLock();
		this.writeLock = lock.writeLock();
		this.readLock = lock.readLock();
	}

	/**
	 * Increases the account's amount by the specified amount. Fails, if upper
	 * limit is exceeded or a negative value is supplied.
	 * 
	 * @param amount
	 */
	public void incrementAmount(long amount) throws IllegalArgumentException, IllegalAccountStateException {
		if (((this.getAmount() + amount) > UPPER_LIMIT)) {
			throw new IllegalAccountStateException();
		} else if (amount <= 0) {
			throw new IllegalArgumentException("Amount must not be <= 0.");
		} else {
			writeLock.lock();
			try {
				this.amount += amount;

			} finally {
				writeLock.unlock();
			}
		}
	}

	/**
	 * Decreases the account's amount by the specified amount. Fails if lower
	 * limit is exceeded or a negative value is supplied.
	 * 
	 * @param amount
	 */
	public void decrementAmount(long amount) throws IllegalArgumentException, IllegalAccountStateException {
		if (((this.getAmount() - amount) < LOWER_LIMIT)) {
			throw new IllegalAccountStateException();
		} else if (amount <= 0) {
			throw new IllegalArgumentException("Amount must not be <= 0.");
		} else {
			writeLock.lock();
			try {
				this.amount -= amount;

			} finally {
				writeLock.unlock();
			}
		}

	}

	/**
	 * Return's the account's owner's name.
	 * 
	 * @return
	 */
	public String getName() {
		String name;
		readLock.lock();
		try {
			name = this.name;
		} finally {
			readLock.unlock();
		}
		return name;
	}

	/**
	 * Sets the account's owner's name. Abort, if new name is empty or starts
	 * with a whitespace.
	 * 
	 * @param name
	 */
	public void setName(String name) throws IllegalArgumentException {
		if (name.length() > 0 && !name.startsWith(" ")) {
			writeLock.lock();
			try {
				this.name = name;
			} finally {
				writeLock.unlock();
			}
		} else {
			throw new IllegalArgumentException("You haven't provided a valid name.");
		}
	}

	/**
	 * Returns the current balance.
	 * 
	 * @return
	 */
	public long getAmount() {
		Long amount;
		readLock.lock();
		try {
			amount = this.amount;
		} finally {
			readLock.unlock();
		}
		return amount;
	}

}