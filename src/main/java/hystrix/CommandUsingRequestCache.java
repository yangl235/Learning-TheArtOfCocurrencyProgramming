package hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 请求Cache
 */
public class CommandUsingRequestCache extends HystrixCommand<Boolean> {
    private final int value;

    public CommandUsingRequestCache(int value) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.value = value;
    }

    @Override
    public Boolean run() {
        return value == 0 || value % 2 == 0;
    }

    //使用cache功能，必须实现该方法
    @Override
    public String getCacheKey() {
        return String.valueOf(value);
    }
}

