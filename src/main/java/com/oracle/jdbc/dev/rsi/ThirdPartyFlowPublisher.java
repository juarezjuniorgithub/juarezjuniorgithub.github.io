package com.oracle.jdbc.dev.rsi;

import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;

import oracle.rsi.ReactiveStreamsIngestion;

public class ThirdPartyFlowPublisher {

	public static void main(String[] args) throws SQLException {

		ExecutorService workerThreadPool = Executors.newFixedThreadPool(2);

		ReactiveStreamsIngestion rsi = ReactiveStreamsIngestion.builder().url("jdbc:oracle:thin:@juarbarb:1521:xe")
				.username("SYSTEM").password("JABJRorcl2022").schema("SYSTEM").executor(workerThreadPool).bufferRows(10)
				.bufferInterval(Duration.ofSeconds(20))
				// .table("customers")
				// .columns(new String[] { "id", "name", "region" })
				.entity(Customer.class).build();

		// JDK's 3rd-party publisher
		SubmissionPublisher<Object[]> publisher = new SubmissionPublisher<>();
		publisher.subscribe(rsi.subscriber());

		publisher.submit(new Object[] { 13, "John Doe", "North" });
		publisher.submit(new Object[] { 14, "Jane Doe", "North" });
		publisher.submit(new Object[] { 15, "John Smith", "South" });

		while (publisher.estimateMaximumLag() > 0)
			;

		try {
			publisher.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		rsi.close();

		workerThreadPool.shutdown();

	}

}
