package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.ModerateLivingEntries;

@Entity(tableName = AppDataBase.HEALTHACTIVITIES_TABLE,
foreignKeys = {@ForeignKey(entity = UserID.class,
parentColumns = "mUserID",
childColumns = "mUserID",
    onDelete = ForeignKey.CASCADE)
})

public class HealthActivities implements ModerateLivingEntries {
  @PrimaryKey(autoGenerate = true)
  private int mActivityID;

  private int mUserID;

  private String mActivityName;
  private String mActivityDescription;
  private int mActivityPoints;
  private boolean mIsRecurring;

  public HealthActivities(int userID,
                          String activityName, String activityDescription,
                          int activityPoints, boolean isRecurring) {
    mUserID = userID;
    mActivityName = activityName;
    mActivityDescription = activityDescription;
    mActivityPoints = activityPoints;
    mIsRecurring = isRecurring;
  }

  public int getActivityID() {
    return mActivityID;
  }

  public void setActivityID(int activityID) {
    mActivityID = activityID;
  }

  @Override
  public String getEntryName() {
    return mActivityName;
  }

  @Override
  public String getDescription() {
    return mActivityDescription;
  }

  @Override
  public int getPoints() {
    return mActivityPoints;
  }

  @Override
  public int getID() {
    return mActivityID;
  }

  public int getUserID() {
    return mUserID;
  }

  public void setUserID(int userID) {
    mUserID = userID;
  }

  public String getActivityName() {
    return mActivityName;
  }

  public void setActivityName(String activityName) {
    mActivityName = activityName;
  }

  public String getActivityDescription() {
    return mActivityDescription;
  }

  public void setActivityDescription(String activityDescription) {
    mActivityDescription = activityDescription;
  }

  public int getActivityPoints() {
    return mActivityPoints;
  }

  public void setActivityPoints(int activityPoints) {
    mActivityPoints = activityPoints;
  }

  public boolean isRecurring() {
    return mIsRecurring;
  }

  public void setRecurring(boolean recurring) {
    mIsRecurring = recurring;
  }

  @Override
  public String toString() {
    return "HealthActivities{" +
        "mActivityID=" + mActivityID +
        ", mUserID='" + mUserID + '\'' +
        ", mActivityName='" + mActivityName + '\'' +
        ", mActivityDescription='" + mActivityDescription + '\'' +
        ", mActivityPoints=" + mActivityPoints +
        ", mIsRecurring=" + mIsRecurring +
        '}';
  }

  public HealthActivities copy() {
    HealthActivities healthActivities = new HealthActivities(
        this.mUserID,
        this.mActivityName,
        this.mActivityDescription,
        this.mActivityPoints,
        this.mIsRecurring);
    return healthActivities;
  }


}
