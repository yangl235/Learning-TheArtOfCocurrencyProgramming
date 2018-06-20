package method_framework.semaphore_exchanger.semaphore;

public class Run {
    public static void main(String[] args) {
        Service service = new Service();
        ThreadA a = new ThreadA(service);
        a.setName("A");
        ThreadA b = new ThreadA(service);
        b.setName("B");
        ThreadA c = new ThreadA(service);
        c.setName("C");
        a.start();
        b.start();
        c.start();
    }
}
