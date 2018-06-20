package hystrix;

import com.netflix.hystrix.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 请求合并就是将一段时间内的多次请求合并为一次请求，常用于网络IO中，能减少IO次数，缺点是增加平均延迟
 */

public class CommandCollapserGetValueForKey extends HystrixCollapser<List<String>, String, Integer> {

    private final Integer key;

    public CommandCollapserGetValueForKey(Integer key) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("Collapser"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter()
                        .withMaxRequestsInBatch(3)
                        .withTimerDelayInMilliseconds(10)));
        this.key = key;
    }

    @Override
    public Integer getRequestArgument() {
        return key;
    }

    @Override
    protected HystrixCommand<List<String>> createCommand(final Collection<CollapsedRequest<String, Integer>> requests) {
        return new BatchCommand(requests);
    }

    @Override
    protected void mapResponseToRequests(List<String> batchResponse, Collection<CollapsedRequest<String, Integer>> requests) {
        int count = 0;
        for (CollapsedRequest<String, Integer> request : requests) {
            request.setResponse(batchResponse.get(count++));
        }
    }

    private static final class BatchCommand extends HystrixCommand<List<String>> {
        private final Collection<CollapsedRequest<String, Integer>> requests;

        private BatchCommand(Collection<CollapsedRequest<String, Integer>> requests) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueForKey")));
            this.requests = requests;
        }

        @Override
        protected List<String> run() {
            System.out.println("BatchCommand run  " + requests.size());
            ArrayList<String> response = new ArrayList<String>();
            for (HystrixCollapser.CollapsedRequest<String, Integer> request : requests) {
                // artificial response for each argument received in the batch
                response.add("ValueForKey: " + request.getArgument());
            }
            return response;
        }
    }

}