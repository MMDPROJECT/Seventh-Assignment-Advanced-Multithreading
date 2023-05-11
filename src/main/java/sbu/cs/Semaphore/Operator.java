package sbu.cs.Semaphore;

import java.util.Date;
import java.util.concurrent.Semaphore;

public class Operator extends Thread {
    Semaphore lock;

    public Operator(String name, Semaphore lock) {
        super(name);
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++)
        {
            try {
                lock.acquire();
                System.out.println(this.getName() + " has accessed to the resource" + " at " + new Date());
                Resource.accessResource();         // critical section - a Maximum of 2 operators can access the resource concurrently
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
