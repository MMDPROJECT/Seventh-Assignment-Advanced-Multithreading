package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    static BigDecimal ans = new BigDecimal(0);

    public static class PiRunnable implements Runnable {

        MathContext mc;
        int floatingPoint;
        BigDecimal i;

        public PiRunnable(int floatingPoint, BigDecimal i) {
            this.floatingPoint = floatingPoint;
            this.mc = new MathContext(floatingPoint + 2);
            this.i = i;
        }

        public void run() {
            BigDecimal term_1 = factorial(i.intValue());
            term_1 = term_1.pow(2);
            BigDecimal term_2 = BigDecimal.valueOf(2).pow(i.intValue() + 1);
            BigDecimal numerator = term_1.multiply(term_2);
            BigDecimal denominator = factorial((2 * i.intValue()) + 1);
            BigDecimal res = numerator.divide(denominator, mc);
            addSum(res);
        }
    }

    public static BigDecimal factorial(int n) {
        if (n == 0) {
            return BigDecimal.valueOf(1);
        } else {
            return BigDecimal.valueOf(n).multiply(factorial(n - 1));
        }
    }

    public static synchronized void addSum(BigDecimal term) {
        ans = ans.add(term);
    }

    /**
     * Calculate pi and represent it as a BigDecimal object with the given floating point number (digits after . )
     * There are several algorithms designed for calculating pi, it's up to you to decide which one to implement.
     * Experiment with different algorithms to find accurate results.
     * <p>
     * You must design a multithreaded program to calculate pi. Creating a thread pool is recommended.
     * Create as many classes and threads as you need.
     * Your code must pass all the test cases provided in the test folder.
     *
     * @param floatingPoint the exact number of digits after the floating point
     * @return pi in string format (the string representation of the BigDecimal object)
     */

    public static String calculate(int floatingPoint) {
        ans = new BigDecimal(0);
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < floatingPoint * 4; i++){
            PiRunnable task = new PiRunnable(floatingPoint, BigDecimal.valueOf(i));
            pool.execute(task);
        }
        try {
            pool.awaitTermination(10000, TimeUnit.MILLISECONDS);
            pool.shutdownNow();
            ans = ans.setScale(floatingPoint, RoundingMode.DOWN);
            return String.valueOf(ans);
        } catch (InterruptedException ie){
            ie.getMessage();
            ie.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        calculate(1000);
    }
}
