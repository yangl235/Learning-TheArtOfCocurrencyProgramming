package core_tech.ch1;

/**
 * 在线程sleep状态下停止线程实验
 */
public class Run {
    public static void main(String [] args) throws InterruptedException{
        SleepInterrupt sleepInterrupt = new SleepInterrupt();
        Thread thread1 = new Thread(sleepInterrupt);
        thread1.start();
        Thread.sleep(200);
        thread1.interrupt();
    }
}
