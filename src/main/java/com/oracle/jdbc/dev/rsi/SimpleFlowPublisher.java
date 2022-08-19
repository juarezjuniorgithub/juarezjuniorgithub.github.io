package com.oracle.jdbc.dev.rsi;

import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oracle.rsi.ReactiveStreamsIngestion;

public class SimpleFlowPublisher {

  public static void main(String[] args) throws SQLException {

    ExecutorService workerThreadPool = Executors.newFixedThreadPool(2);

    ReactiveStreamsIngestion rsi = ReactiveStreamsIngestion.builder().url("jdbc:oracle:thin:@juarbarb:1521:xe")
			.username("SYSTEM").password("JABJRorcl2022").schema("SYSTEM").executor(workerThreadPool).bufferRows(10)
			.bufferInterval(Duration.ofSeconds(20))
			// .table("customers")
			// .columns(new String[] { "id", "name", "region" })
			.entity(Customer.class).build();

    SimpleObjectPublisher<Object[]> publisher = new SimpleObjectPublisher<Object[]>();
    publisher.subscribe(rsi.subscriber());

    SimpleObjectPublisher<Object[]> anotherPublisher = new SimpleObjectPublisher<Object[]>();
    anotherPublisher.subscribe(rsi.subscriber());
    
    publisher.accept(new Object[] { 7, "John Doe", "North" });
    publisher.accept(new Object[] { 8, "Jane Doe", "North" });
    publisher.accept(new Object[] { 9, "John Smith", "South" });

    anotherPublisher.accept(new Object[] { 10, "John Doe", "North" });
    anotherPublisher.accept(new Object[] { 11, "Jane Doe", "North" });
    anotherPublisher.accept(new Object[] { 12, "John Smith", "South" });
    
    rsi.close();

    workerThreadPool.shutdown();

  }

}



