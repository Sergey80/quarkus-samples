package my.project.subject;

import io.reactivex.Single;

import io.restassured.response.Response;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class LoadTestUtils {

    public static LoadTestMetricsData callPeriodically(final Supplier<Single<Response>> restCallFunction, long callnTimes, long callEveryMilisseconds) throws InterruptedException {

        final long start = System.currentTimeMillis();

        final List<Single<Response>> listOfSingleResponses = Collections.synchronizedList(new ArrayList<>());

        // final Observable<Long> timeSpace = Observable.interval(callEveryMilisseconds, TimeUnit.MILLISECONDS).take(callnTimes);

        for(int i = 0; i < callnTimes; i++) {

            Thread.sleep(callEveryMilisseconds);

            Single<Response> responseSingle = restCallFunction.get();
            listOfSingleResponses.add(responseSingle);
        }

        return Single.zip(listOfSingleResponses, (Object[] objects) -> {

            final LoadTestMetricsData loadTestMetricsData = new LoadTestMetricsData();

            final Response[] arrayOfResponses = Arrays.copyOf(objects, objects.length, Response[].class);

            loadTestMetricsData.responseList = Arrays.asList(arrayOfResponses);

            loadTestMetricsData.duration = Duration.ofMillis(System.currentTimeMillis() - start);

            return loadTestMetricsData;

        }).blockingGet();

    }
}
