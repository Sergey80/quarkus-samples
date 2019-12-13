package my.project.subject.application.performance;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import my.project.subject.api.UserData;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/performance")
public class PerformanceTestResourceRx {

    //@Timeout(20000)
    @GET
    @Path("/resource/rx")
    @Produces(MediaType.APPLICATION_JSON)
    public Single<UserData> performanceResource() {

        final String name = Thread.currentThread().getName();

        System.out.println(name);
        System.out.println("name1: " + name);
        System.out.println( System.currentTimeMillis());

        return Single.fromCallable(() -> {

            final String name2 = Thread.currentThread().getName();
            System.out.println("name2: " + name2);

            Thread.sleep(1000);
            return new UserData();
        }).subscribeOn(Schedulers.io());
    }

}
