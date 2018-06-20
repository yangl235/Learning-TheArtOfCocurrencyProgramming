package method_framework.semaphore_exchanger.semaphore;

import java.util.concurrent.Semaphore;

public class Service {
    private Semaphore semaphore = new Semaphore(4);

    public void testMethod() {
        try {
            //申请信号量
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " begin timer=" + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + " end timer=" + System.currentTimeMillis());
            //释放信号量
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
