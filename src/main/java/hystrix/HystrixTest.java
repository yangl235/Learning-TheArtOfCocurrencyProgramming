package hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HystrixTest {
    @Test
    public void hystrixTest() {
        String s = new CommandHelloWorld("Bob").execute();
        /*Future<String> s = new CommandHelloWorld("Bob").queue();
        Observable<String> s = new CommandHelloWorld("Bob").observe();*/
    }

    @Test
    public void maxCurrentRequst() throws InterruptedException {
        int count = 10;
        while (count > 0) {
            int id = count--;
            new Thread(() -> {
                try {
                    new CommandUsingSemaphoreIsolation(id).execute();
                } catch (Exception ex) {
                    System.out.println("Exception:" + ex.getMessage() + " id=" + id);
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(100);
    }
//注：使用信号量隔离，在同一个线程中即使循环调用new CommandUsingSemaphoreIsolation(id).queue()，run方法也是顺序执行;

    /**
     * fallback()方法测试
     */
    @Test
    public void fallbackTest() {
        assertEquals("Hello Fallback", new CommandHelloWorld("World").execute());
    }

    /**
     * 测试由异常导致的fallback
     */
    @Test
    public void expTest() {
        assertEquals("Hello Failure Exception!", new CommandHelloFailure("Exception").execute());
    }

    /**
     * 测试有超时导致的fallback
     */
    @Test
    public void timeOutTest() {
        assertEquals("Hello Failure Timeout!", new CommandHelloFailure("Timeout").execute());
    }

    /**
     * 并发执行的任务数超过线程池和队列之和会被reject，导致fallback
     *
     * @throws InterruptedException
     */
    @Test
    public void rejectTest() throws InterruptedException {
        int count = 5;
        while (count-- > 0) {
            new CommandHelloFailure("Reject").queue();
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    /**
     * 请求合并测试
     *
     * @throws Exception
     */
    @Test
    public void testCollapser() throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            Future<String> f1 = new CommandCollapserGetValueForKey(1).queue();
            Future<String> f2 = new CommandCollapserGetValueForKey(2).queue();
            Future<String> f3 = new CommandCollapserGetValueForKey(3).queue();
            Future<String> f4 = new CommandCollapserGetValueForKey(4).queue();


            assertEquals("ValueForKey: 1", f1.get());
            assertEquals("ValueForKey: 2", f2.get());
            assertEquals("ValueForKey: 3", f3.get());
            assertEquals("ValueForKey: 4", f4.get());

            // assert that the batch command 'GetValueForKey' was in fact
            // executed and that it executed only once
            assertEquals(2, HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size());
            HystrixCommand<?> command = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().toArray(new HystrixCommand<?>[1])[0];
            // assert the command is the one we're expecting
            assertEquals("GetValueForKey", command.getCommandKey().name());
            // confirm that it was a COLLAPSED command execution
            assertTrue(command.getExecutionEvents().contains(HystrixEventType.COLLAPSED));
            // and that it was successful
            assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
        } finally {
            context.shutdown();
        }
    }

    /**
     * 请求cache测试
     */
    @Test
    public void testWithCacheHits() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            CommandUsingRequestCache command2a = new CommandUsingRequestCache(2);
            CommandUsingRequestCache command2b = new CommandUsingRequestCache(2);

            assertTrue(command2a.execute());
            //第一次请求，没有cache
            assertFalse(command2a.isResponseFromCache());

            assertTrue(command2b.execute());
            // 第二次请求，从cache中拿的结果
            assertTrue(command2b.isResponseFromCache());
        } finally {
            context.shutdown();
        }

        context = HystrixRequestContext.initializeContext();
        try {
            CommandUsingRequestCache command3b = new CommandUsingRequestCache(2);
            assertTrue(command3b.execute());
            // this is a new request context so this
            //new了新的 request context后，之前的cache失效
            assertFalse(command3b.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }
}
