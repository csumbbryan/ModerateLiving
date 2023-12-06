package com.example.moderateliving;

import android.widget.TextView;

//TODO: See if this could be used for multiple records
public class HealthEntry {
  private String mEntryName;
  private String mEntryDescription;
  private String mEntryPoints;

  public HealthEntry(String entryName, String entryDescription, int entryPoints) {
    mEntryName = entryName;
    mEntryDescription = entryDescription;
    mEntryPoints = entryPoints == 1 ? entryPoints + " Pt" : entryPoints + " Pts";
  }

  public String getEntryName() {
    return mEntryName;
  }

  public void setEntryName(String entryName) {
    mEntryName = entryName;
  }

  public String getEntryDescription() {
    return mEntryDescription;
  }

  public void setEntryDescription(String entryDescription) {
    mEntryDescription = entryDescription;
  }

  public String getEntryPoints() {
    return mEntryPoints;
  }

  public void setEntryPoints(String entryPoints) {
    mEntryPoints = entryPoints;
  }
}
