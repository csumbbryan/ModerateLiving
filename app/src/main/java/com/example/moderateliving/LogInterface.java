package com.example.moderateliving;

import java.time.LocalDate;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: defines methods required for any class requiring generic log access
 */
public interface LogInterface {
  public Integer getEntryID();

  public int getLogID();

  public int getUserID();

  public LocalDate getCreateDate();
  public LocalDate getCompleteDate();

  public int getPoints();
}
