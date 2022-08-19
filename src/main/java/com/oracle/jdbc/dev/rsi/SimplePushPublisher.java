package com.oracle.jdbc.dev.rsi;

import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oracle.rsi.ReactiveStreamsIngestion;
import oracle.rsi.PushPublisher;

public class SimplePushPublisher {

	public static void main(String[] args) throws SQLException {

		ExecutorService workerThreadPool = Executors.newFixedThreadPool(2);

		ReactiveStreamsIngestion rsi = ReactiveStreamsIngestion.builder().url("jdbc:oracle:thin:@juarbarb:1521:xe")
				.username("SYSTEM").password("JABJRorcl2022").schema("SYSTEM").executor(workerThreadPool).bufferRows(10)
				.bufferInterval(Duration.ofSeconds(20))
				// .table("customers")
				// .columns(new String[] { "id", "name", "region" })
				.entity(Customer.class).build();

		// RSI publisher
		PushPublisher<Object[]> firstPublisher = ReactiveStreamsIngestion.pushPublisher();
		firstPublisher.subscribe(rsi.subscriber());
		firstPublisher.accept(new Object[] { 1, "Nick Smyths", "South" });
		firstPublisher.accept(new Object[] { 2, "Jane Melina", "North" });
		firstPublisher.accept(new Object[] { 3, "John Gosling", "South" });

		// Another RSI publisher
		PushPublisher<Object[]> secondPublisher = ReactiveStreamsIngestion.pushPublisher();
		firstPublisher.subscribe(rsi.subscriber());
		secondPublisher.accept(new Object[] { 4, "Junior Gupta", "North" });
		secondPublisher.accept(new Object[] { 5, "Jack Doe", "North" });
		secondPublisher.accept(new Object[] { 6, "Steff Cazado", "South" });
		try {
			firstPublisher.close();
			secondPublisher.close();
			rsi.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerThreadPool.shutdown();
		}

	}

}
