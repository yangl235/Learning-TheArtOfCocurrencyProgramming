package method_framework.semaphore_exchanger.semaphore_acquire;


public class Run {
    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        ThreadA[] a = new ThreadA[10];
        for (int i = 0; i < 10; i++) {
            a[i] = new ThreadA(service);
            a[i].start();
        }
    }
}
