package my.project.subject;

import io.restassured.response.Response;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LoadTestMetricsData {
    public List<Response> responseList = new ArrayList<>();
    public Duration duration;
}
