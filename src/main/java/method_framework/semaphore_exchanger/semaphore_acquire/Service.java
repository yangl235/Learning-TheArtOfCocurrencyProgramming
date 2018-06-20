package method_framework.semaphore_exchanger.semaphore_acquire;

import java.util.concurrent.Semaphore;

/**
 * @author yanglei
 */
public class Service {
    private Semaphore semaphore = new Semaphore(10);

    public void testMethod() {
        try {
            //申请信号量
            semaphore.acquire(2);
            System.out.println(Thread.currentThread().getName() + " begin timer=" + System.currentTimeMillis());
            int sleepValue = (int) (Math.random() * 1000);
            System.out.println(Thread.currentThread().getName() + "停止了" + sleepValue / 1000 + "秒");
            Thread.sleep(sleepValue);
            System.out.println(Thread.currentThread().getName() + " end timer=" + System.currentTimeMillis());
            //释放信号量
            semaphore.release(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
