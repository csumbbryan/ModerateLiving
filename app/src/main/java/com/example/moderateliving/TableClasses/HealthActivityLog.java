package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.LogInterface;

import java.time.LocalDate;

@Entity(tableName = AppDataBase.HEALTHACTIVITIES_LOG_TABLE,
    foreignKeys = {@ForeignKey(entity = UserID.class,
        parentColumns = "mUserID",
        childColumns = "mUserID",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = HealthActivities.class,
        parentColumns = "mActivityID",
        childColumns = "mActivityID",
        onDelete = ForeignKey.SET_NULL)
    })
public class HealthActivityLog implements LogInterface {

  @PrimaryKey(autoGenerate = true)
  private int mActivityLogID;

  private Integer mActivityID; //Set this as Integer to support ForeignKey.SET_NULL
  private int mUserID;
  private LocalDate mDateCreated;
  private LocalDate mDateCompleted;
  private boolean mIsComplete;
  private int mPointsEarned;

  public HealthActivityLog(int activityLogID, int activityID, int userID,
                           LocalDate dateCreated, LocalDate dateCompleted,
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

  public Integer getActivityID() {
    return mActivityID;
  }

  public void setActivityID(int activityID) {
    mActivityID = activityID;
  }

  @Override
  public int getEntryID() {
    return getActivityID();
  }

  @Override
  public int getLogID() {
    return getActivityLogID();
  }

  public int getUserID() {
    return mUserID;
  }

  @Override
  public LocalDate getCreateDate() {
    return getDateCreated();
  }

  @Override
  public LocalDate getCompleteDate() {
    return getDateCompleted();
  }

  @Override
  public int getPoints() {
    return getPointsEarned();
  }

  public void setUserID(int userID) {
    mUserID = userID;
  }

  public LocalDate getDateCreated() {
    return mDateCreated;
  }

  public void setDateCreated(LocalDate dateCreated) {
    mDateCreated = dateCreated;
  }

  public LocalDate getDateCompleted() {
    return mDateCompleted;
  }

  public void setDateCompleted(LocalDate dateCompleted) {
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
