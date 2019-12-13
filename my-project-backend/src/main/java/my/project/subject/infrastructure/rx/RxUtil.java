package my.project.subject.infrastructure.rx;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.concurrent.CompletableFuture;

public class RxUtil {

    public static <T> Single<T> toSingle(CompletableFuture<T> future) {
        return Single.fromObservable(Observable.create(subscriber ->
                future.whenComplete((result, error) -> {
                    if (error != null) {
                        subscriber.onError(error);
                    } else {
                        subscriber.onNext(result);
                        subscriber.onComplete();
                    }
                })));
    }
}
