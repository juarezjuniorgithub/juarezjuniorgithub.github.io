package com.oracle.jdbc.dev.rsi;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Consumer;

public class SimpleObjectPublisher<T> implements Publisher<T>, Consumer<T> {

	  Subscriber<? super T> subscriber;
	  
	  Subscription subscription = new SimpleObjectSubscription(); 
	 
	 //Data streaming starts

	  @Override
	  public void subscribe(Subscriber<? super T> subscriber) {
	    this.subscriber = subscriber;
	    this.subscriber.onSubscribe(subscription);
	  }
	  
	  @Override
	  public void accept(T t) {
	    subscriber.onNext(t);
	  }
	  
	}