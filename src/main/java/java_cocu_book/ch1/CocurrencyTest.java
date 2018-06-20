package java_cocu_book.ch1;

/**
 * 当并发执行累加操作不超过某个阈值时，速度会比串行执行要慢，因为线程会有创建和切换上下文的开销
 */
public class CocurrencyTest {
    private static final long count = 1000L;

    public static void main(String[] args) throws InterruptedException {
        cocurrency();
        serial();
    }

    /**
     * @throws InterruptedException
     */
    public static void cocurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int a = 0;
                for (long i = 0; i < count; i++) {
                    a += 5;
                }
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        thread.join();
        System.out.println("concurrency: " + time + "ms,b=" + b);
    }

    public static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial: " + time + "ms,b=" + b + " a=" + a);
    }
}
