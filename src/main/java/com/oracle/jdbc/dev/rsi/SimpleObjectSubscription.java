package com.oracle.jdbc.dev.rsi;

import java.util.concurrent.Flow.Subscription;

// You must provide this subscription
class SimpleObjectSubscription implements Subscription {

	@Override
	public void request(long n) {
		System.out.println("Library requesting: " + n + " records");
	}

	@Override
	public void cancel() {
		System.out.println("Cancelled!");
	}

}