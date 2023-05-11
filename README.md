![](https://s8.uupload.ir/files/untitled_513.png)

# 						Seventh Assignment Report

## 													   Mohammad Hossein Basouli-401222020

















## Introduction:

- #### A brief description of the assignment:

  - In this assignment we were supposed to learn how to handle **race condition** in **multi-threaded** programs such adding manipulating a **shared resource** and .. .

- #### Objectives of the assignment:

  - Find an algorithm to calculate **pi**.
  - Handling **race condition**.
  - Learn how to use **cached/fixed thread pool**.
  - **Scheduling** running **threads** and **tasks**.
  - Learn how to use **semaphore locks**.



## Design and Implementation:

- #### A description on design of the solution:

  - ##### **PiCalculator:**

    1. We are gonna use a algorithm called **" Gregory-Leibniz"** which it's formula is brought down here:
       $$
       \sum_{i=0}^{\infty} \frac{(i!)^2 \cdot 2^{i+1}}{2i+1}
       $$

    $$
    
    $$

    2. We initialize the **tasks** to calculate each term of the **pi** and then assign each of them to a **thread** to execute it.

    3. We use a **synchronized** function to handle **race condition** while we are adding the terms together.

       ##### public static synchronized void addSum(BigDecimal term) {


        ans = ans.add(term);


}
    
    4. After we assigned the **tasks** to **threads**, it's necessary to **shutdown** the **thread pool**. Then we have to tell the **thread pool** to wait a specified time for threads to get finished, otherwise desired result is not gonna be returned.
    
    5. At the end it's necessary to specify the scale that we want to return. We can do it using **setScale**().

       ```java
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
       ```
    
    
  
  - ##### **Priority Simulator:**
  
    1. There are three types of **tasks** (**Black**, **Blue** and **White**) that we have to execute in a special order. (**Black > Blue White**)
  
    2. We could use **"CountDownLatch"** class for each one of the **tasks**, to make sure order is kept during execution of the program. here is the implementation: 
  
       ```java
       public void run(int blackCount, int blueCount, int whiteCount) throws InterruptedException {
               List<ColorThread> colorThreads = new ArrayList<>();
       
               CountDownLatch countDownLatch_blacks = new CountDownLatch(blackCount);
               CountDownLatch countDownLatch_blues = new CountDownLatch(blueCount);
               CountDownLatch countDownLatch_whites = new CountDownLatch(whiteCount);
       
               // TODO
       
               for (int i = 0; i < blackCount; i++) {
                   BlackThread blackThread = new BlackThread(countDownLatch_blacks);
                   colorThreads.add(blackThread);
                   blackThread.start();
               }
               countDownLatch_blacks.await();
       
               // TODO
       
               for (int i = 0; i < blueCount; i++) {
                   BlueThread blueThread = new BlueThread(countDownLatch_blues);
                   colorThreads.add(blueThread);
                   blueThread.start();
               }
               countDownLatch_blues.await();
       
               // TODO
       
               for (int i = 0; i < whiteCount; i++) {
                   WhiteThread whiteThread = new WhiteThread(countDownLatch_whites);
                   colorThreads.add(whiteThread);
                   whiteThread.start();
               }
               countDownLatch_whites.await();
       
               // TODO
           }
       ```
  
    3. Here is the implementation to how use **"CountDownLatch"** in each one of the classes:
  
       ```java
       public class BlackThread extends ColorThread {
       .....
           private CountDownLatch countDownLatch;
       
           public BlackThread(CountDownLatch countDownLatch) {
               this.countDownLatch = countDownLatch;
           }
       .....
           @Override
           public void run() {
               // TODO call printMessage
       		//Do some work
               countDownLatch.countDown();
       }
       ```
  
       
  
  
  
  - ##### **Semaphore:**
  
    1. We are supposed to use a **"Semaphore Lock"** to be able to let **multiple threads** have access to the **shared resource**. Here is the implementation to how initialize a **"Semaphore Lock"** and assign it to **threads**:
  
       ```java
       Semaphore lock = new Semaphore(2);
               Operator operator1 = new Operator("operator1", lock);
               Operator operator2 = new Operator("operator2", lock);
               Operator operator3 = new Operator("operator3", lock);
               Operator operator4 = new Operator("operator4", lock);
               Operator operator5 = new Operator("operator5", lock);
       ```
  
    2. Here we show how each **thread** can access the **shared resource** using **acquire()**:
  
       ```java
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
                       ......
                       lock.release();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
               }
           }
       }
       ```
  
       



- #### A brief explanation about libraries has been used:

  - **BigDecimal:** Is used to increase the precision while we are working with floating point numbers, Which has it's own methods to do operation with numbers such as:

    1. **pow().**

    2. **multiply().**

    3. **divide().**

    4. **subtract().**

    5. **setScale().**

       

  - **CachedThreadPool:** Is used to handle executing short-lived **threads**. It will assign unused **threads** instead of constructing a whole new set of them. Here are some of the methods of it's class:

    1. **execute()**: This will execute the thread which we assign. 

    2. **shutdown()**: It will prevent the thread pool to accept new threads which. This is necessary to shutdown the thread pool after all the tasks has been assigned.

    3. **awaitTermination()**: It wait for a specified time for threads to get terminated, otherwise it will raise a false flag that could help us to know what do to next.

    4. **shutdownNow()**: This will force all the unterminated threads.

       

  - **CountDownLatch:** A **synchronization** aid that allows one or more **threads** to wait until a set of operations being performed in other **threads** completes. Here are some of it's methods:

    1. **await()**: Causes the current thread to wait until the latch has counted down to zero, unless the thread is interrupted.

    2. **countDown()**: Decrements the count of the latch, releasing all waiting threads if the count reaches zero.

       

  - **Semaphore:** **Semaphores** are often used to restrict the number of **threads** than can access some (physical or logical) resource.

    1. **acquire()**: Takes one of the available permit, otherwise waits until one permit becomes available.

    2. **release()**: Releases the taken permit.

       


$$

$$











