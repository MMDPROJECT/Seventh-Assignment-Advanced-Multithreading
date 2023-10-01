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

        for (int i = 0; i < 4000; i++){
            PiRunnable task = new PiRunnable(floatingPoint, BigDecimal.valueOf(i));
            pool.execute(task);
        }
        pool.shutdown();
        try {
            if (pool.awaitTermination(20000, TimeUnit.MILLISECONDS)){
                ans = ans.setScale(floatingPoint, RoundingMode.DOWN);
                System.out.println(ans);
                System.out.println("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989");
                return String.valueOf(ans);
            } else {
                pool.shutdownNow();
            }
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
