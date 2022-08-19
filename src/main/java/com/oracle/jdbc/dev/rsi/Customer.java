package com.oracle.jdbc.dev.rsi;

import oracle.rsi.StreamEntity;
import oracle.rsi.StreamField;

@StreamEntity(tableName = "customers")
public class Customer {

  public Customer(long id, String name, String region) {
    super();
    this.id = id;
    this.name = name;
    this.region = region;
  }

  @StreamField
  public long id;

  @StreamField
  public String name;

  @StreamField(columnName = "region")
  public String region;

  String someRandomField;

}