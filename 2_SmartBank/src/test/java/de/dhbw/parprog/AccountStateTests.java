package de.dhbw.parprog;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class AccountStateTests {
    Bank bank;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setupTest() {
        bank = new Bank();
    }

    @Test
    public void newAccountHasNoCredits() {
        Account a = bank.createAccount();
        assertThat(bank.getBalance(a)).isEqualTo(0);
    }

    @Test
    public void canDepositAndWithdraw() throws IllegalAccountStateException {
        Account a = bank.createAccount();
        bank.deposit(a, 10);
        bank.withdraw(a, 5);
    }

    @Test
    public void cantDepositMoreThanMaximum() throws IllegalAccountStateException {
        Account a = bank.createAccount();
        bank.deposit(a, Account.UPPER_LIMIT);
        thrown.expect(IllegalAccountStateException.class);
        bank.deposit(a, 1);
        Assert.fail("Should have caused an exception");
    }

    @Test
    public void cantWithdrawBelowMinimum() throws IllegalAccountStateException {
        Account a = bank.createAccount();
        thrown.expect(IllegalAccountStateException.class);
        bank.withdraw(a, 1);
        Assert.fail("Should have caused an exception");
    }
}
