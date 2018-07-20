package com.hitsme.locker.app.bus;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by 10093 on 2017/4/10.
 */


public class EventBus {

    private static EventBus instance;

    private PublishSubject<Event> subject = PublishSubject.create();

    public static  EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }


    public void addEvent(Event object) {
        subject.onNext(object);
    }


    public Observable<Event> getEvents() {
        return subject;
    }
}