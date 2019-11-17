import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 17 Нояб., 2019
 */
public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Bank bank = new Bank();
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Account account = new Account((random.nextInt(10) * 50000 + 500_000) * 10, "000" + i);
            bank.addAccount(account);
        }
        ArrayList<Account> accounts = bank.getAccounts();
        for (int i = 0; i < 100000; i++) {
            Thread t = new Thread(() -> {
                //на > 50000 будет как раз 5%
                int randomAmount = random.nextInt(52632);
                if (randomAmount > 50000) {
                    System.out.println(randomAmount);
                }
                bank.transfer(accounts.get(random.nextInt(accounts.size())).getAccNumber(),
                        accounts.get(random.nextInt(accounts.size())).getAccNumber(),
                        randomAmount);
            });
            t.start();
        }
        int blockedCount = 0;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).isBlocked()) {
                blockedCount++;
            }
        }
        System.out.println("blockedCount: " + blockedCount);
        System.out.println("время выполнения: " + (System.currentTimeMillis() - time));
    }
}
