package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
@Entity(tableName = AppDataBase.HEALTHACTIVITIES_LOG_TABLE,
    foreignKeys = {@ForeignKey(entity = UserID.class,
        parentColumns = "mUserID",
        childColumns = "mUserID",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = HealthActivities.class,
        parentColumns = "mActvivityID",
        childColumns = "mActivityID",
        onDelete = ForeignKey.CASCADE)
    })
public class HealthActivityLog {

  @PrimaryKey(autoGenerate = true)
  private int mActivityLogID;

  private int mActivityID;
  private int mUserID;
  private String mDateCreated;
  private String mDateCompleted;
  private boolean mIsComplete;
  private int mPointsEarned;

  public HealthActivityLog(int activityLogID, int activityID, int userID,
                           String dateCreated, String dateCompleted,
                           boolean isComplete, int pointsEarned) {
    mActivityLogID = activityLogID;
    mActivityID = activityID;
    mUserID = userID;
    mDateCreated = dateCreated;
    mDateCompleted = dateCompleted;
    mIsComplete = isComplete;
    mPointsEarned = pointsEarned;
  }

  public int getActivityLogID() {
    return mActivityLogID;
  }

  public void setActivityLogID(int activityLogID) {
    mActivityLogID = activityLogID;
  }

  public int getActivityID() {
    return mActivityID;
  }

  public void setActivityID(int activityID) {
    mActivityID = activityID;
  }

  public int getUserID() {
    return mUserID;
  }

  public void setUserID(int userID) {
    mUserID = userID;
  }

  public String getDateCreated() {
    return mDateCreated;
  }

  public void setDateCreated(String dateCreated) {
    mDateCreated = dateCreated;
  }

  public String getDateCompleted() {
    return mDateCompleted;
  }

  public void setDateCompleted(String dateCompleted) {
    mDateCompleted = dateCompleted;
  }

  public boolean isComplete() {
    return mIsComplete;
  }

  public void setComplete(boolean complete) {
    mIsComplete = complete;
  }

  public int getPointsEarned() {
    return mPointsEarned;
  }

  public void setPointsEarned(int pointsEarned) {
    mPointsEarned = pointsEarned;
  }

  @Override
  public String toString() {
    return "HealthActivityLog{" +
        "mActivityLogID=" + mActivityLogID +
        ", mActivityID=" + mActivityID +
        ", mUserID=" + mUserID +
        ", mDateCreated='" + mDateCreated + '\'' +
        ", mDateCompleted='" + mDateCompleted + '\'' +
        ", mIsComplete=" + mIsComplete +
        ", mPointsEarned=" + mPointsEarned +
        '}';
  }
}
