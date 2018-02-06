package ru.gdgkazan.rxjavasamples.tasks;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class RxJavaTask1 {

    /**
     * TODO : implement this method
     * <p>
     * This method takes list of strings and creates an observable of integers,
     * that represents length of strings which contains letter 'r' (or 'R')
     * <p>
     * Example:
     * Input list: ("Vasya", "Dima", "Artur", "Petya", "Roma")
     * Result stream: (5, 4)
     */
    @NonNull
    public static Observable<Integer> task1(@NonNull List<String> list) {

        //первый вариант
        //List<Integer> p = list.stream().filter(str -> str.contains("r") || str.contains("R")).map(String::length).collect(Collectors.toList());

        //второй вариант
        return Observable.from(list).filter(s -> s.contains("r") || s.contains("R")).map(String::length);


       /* for (String str : par) {
            if (str.con) {
                Observable.just(String::length);
            }
        }*/
    }

}