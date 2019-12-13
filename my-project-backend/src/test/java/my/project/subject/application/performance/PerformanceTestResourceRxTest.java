package my.project.subject.application.performance;

import my.project.subject.LoadTestMetricsData;
import my.project.subject.LoadTestUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;

@QuarkusTest // Comment if test against running app
public class PerformanceTestResourceRxTest {

        //@Tag("load-test")
        //@Test // TODO:
        public void loadTest() throws InterruptedException {

            int CALL_N_TIMES = 1000;
            final long CALL_NIT_EVERY_MILLISECONDS = 0;

            final LoadTestMetricsData loadTestMetricsData = LoadTestUtils.callPeriodically(
                                                                                    this::callHttpEndpoint,
                                                                                    CALL_N_TIMES,
                                                                                    CALL_NIT_EVERY_MILLISECONDS
                                                                            );

            assertThat(loadTestMetricsData.responseList.size(), CoreMatchers.is(equalTo(Long.valueOf(CALL_N_TIMES).intValue())));

            long executionTime = loadTestMetricsData.duration.getSeconds();

            System.out.println("executionTime: " + executionTime + " seconds");

            assertThat(executionTime , allOf(greaterThanOrEqualTo(1L),lessThan(20L)));
        }

        // --

        public Single<Response> callHttpEndpoint() {
            try {

                return Single.fromCallable(() -> {
                    final Response response = given()
                            //.pathParam("name", uuid)
                            .when().get(/*"http://localhost:8080/my-project-service" +*/ "/performance/resource/rx"); // http://localhost:8080/my-project-service
                    return response;
                }).subscribeOn(Schedulers.io());
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
}
