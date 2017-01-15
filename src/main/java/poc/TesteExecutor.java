package poc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by thiag on 14/01/2017.
 */
public class TesteExecutor {
    static Semaphore s = new Semaphore(0);
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(4);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("BOO");
                s.release();
            }
        };
        es.execute(r);
        try {
            s.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
        try {
            es.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdownNow();
    }
}
