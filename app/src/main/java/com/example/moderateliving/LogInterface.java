package com.example.moderateliving;

import java.time.LocalDate;

public interface LogInterface {
  public int getEntryID();

  public int getLogID();

  public int getUserID();

  public LocalDate getCreateDate();
  public LocalDate getCompleteDate();

  public int getPoints();



}
