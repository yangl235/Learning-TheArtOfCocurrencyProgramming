package hystrix;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class CommandHelloWorld extends HystrixCommand<String> {

    private static final Logger LOG = LoggerFactory.getLogger(CommandHelloWorld.class);


    private final String name;

    public CommandHelloWorld(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))  //必须
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(500))  //超时时间
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ExampleGroup-pool"))  //可选,默认 使用 this.getClass().getSimpleName();
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(4)));

        this.name = name;
    }

    @Override
    protected String run() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("running");
        return "Hello " + name + "!";
    }

    /**
     * 当run方法运行出异常时，hystrix提供fallback()方法提供降级方案
     */
    @Override
    protected String getFallback() {
        return "Hello " + "Fallback";
    }
}
