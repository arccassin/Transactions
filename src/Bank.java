import java.util.*;

public class Bank {
    private final Random random = new Random();
    private HashMap<String, Account> accounts = new HashMap<>();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        if (fromAccountNum.equals(toAccountNum)) {
            return;
        }
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);
        //технический овердрафт возможен
        synchronized (fromAccount) {
            if (fromAccount.getMoney() >= amount) {
                fromAccount.subtractAmount(amount);
            }
        }
        synchronized (toAccount) {
            toAccount.addAmount(amount);
        }
        if (amount > 50000) {
            try {
                if (isFraud(fromAccountNum, toAccountNum, amount)) {
                    fromAccount.setBlocked(true);
                    toAccount.setBlocked(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        Account account = accounts.get(accountNum);
        return account.getMoney();
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccNumber(), account);
    }

    public ArrayList<Account> getAccounts(){

        return new ArrayList<>(accounts.values());
    }
}
