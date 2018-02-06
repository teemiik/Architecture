package ru.gdgkazan.rxjavasamples.tasks;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * @author Artur Vasilov
 */
public class RxJavaTask4 {

    /**
     * TODO :
     * <p>
     * Method takes boolean stream of single boolean value
     * and two numbers streams
     * <p>
     * If boolean value in stream it true, then you should choose first stream of number,
     * in other case - second
     * <p>
     * If result stream has any number greater than 99, your observable should finish with error
     * <p>
     * Examples:
     * Input boolean stream: (true)
     * First input stream: (5, 19, 12)
     * Second input stream: (9, 210, 87)
     * Result stream: (5, 19, 12)
     * <p>
     * Input boolean stream: (false)
     * First input stream: (5, 19, 12)
     * Second input stream: (9, 210, 87)
     * Result stream: (9, Exception)
     */
    @NonNull
    public static Observable<Integer> task4(@NonNull Observable<Boolean> flagObservable,
                                            @NonNull Observable<Integer> first, @NonNull Observable<Integer> second) {

        final Boolean[] flag = new Boolean[1];

        flagObservable.subscribe(aBoolean -> flag[0] = aBoolean);

        if (flag[0]) {
            return first.map(integer -> {
                if (integer <= 99) {
                    return integer;
                } else {
                    throw new RuntimeException();
                }
            });
        } else {
            return second.map(integer -> {
                if (integer <= 99) {
                    return integer;
                } else {
                    throw new RuntimeException();
                }
            });
        }
    }

}
