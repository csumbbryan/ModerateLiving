package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
@Entity(tableName = AppDataBase.HEALTHACTIVITIES_TABLE,
foreignKeys = {@ForeignKey(entity = UserID.class,
parentColumns = "mUserID",
childColumns = "mUserID",
    onDelete = ForeignKey.CASCADE)
})

public class HealthActivities {
  @PrimaryKey(autoGenerate = true)
  private int mActivityID;

  private String mUserID;

  private String mActivityName;
  private String mActivityDescription;
  private int mActivityPoints;
  private boolean mIsRecurring;

  public HealthActivities(int activityID, String userID,
                          String activityName, String activityDescription,
                          int activityPoints, boolean isRecurring) {
    mActivityID = activityID;
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

  public String getUserID() {
    return mUserID;
  }

  public void setUserID(String userID) {
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
}
