package core_tech.ch1;

public class SleepInterrupt extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            System.out.println("线程在sleep()时被中断！");
            System.out.println(this.isInterrupted());
            this.isInterrupted();
            e.printStackTrace();
        }
    }

}
