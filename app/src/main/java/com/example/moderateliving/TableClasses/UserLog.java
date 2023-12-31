package com.example.moderateliving.TableClasses;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;

import java.time.LocalDate;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: UserLog table entry Java object representation
 */
//TODO: Consider LiveData?
@Entity(tableName = AppDataBase.USER_LOG_TABLE,
    foreignKeys = {@ForeignKey(entity = UserID.class,
        parentColumns = "mUserID",
        childColumns = "mUserID",
        onDelete = ForeignKey.CASCADE)
    })
public class UserLog {
  @PrimaryKey (autoGenerate = true)
  private int mLogID;
  private int mItemID;
  private int mUserID;
  private String mLogEntryName;
  private String mUsername;
  private String mDescription;
  private String mActivityType;
  private LocalDate mTransactionDate;
  private int mPoints;

  public UserLog(int userID, int itemID, String logEntryName, String username, String description, String activityType, LocalDate transactionDate, int points) {
    mUserID = userID;
    mItemID = itemID;
    mLogEntryName = logEntryName;
    mUsername = username;
    mDescription = description;
    mActivityType = activityType;
    mTransactionDate = transactionDate;
    mPoints = points;
  }

  public int getLogID() {
    return mLogID;
  }

  public void setLogID(int logID) {
    mLogID = logID;
  }

  public String getLogEntryName() {
    return mLogEntryName;
  }

  public void setLogEntryName(String logEntryName) {
    mLogEntryName = logEntryName;
  }

  public int getItemID() {
    return mItemID;
  }

  public void setItemID(int mItemID) {
    this.mItemID = mItemID;
  }

  public int getUserID() {
    return mUserID;
  }

  public void setUserID(int mUserID) {
    this.mUserID = mUserID;
  }

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(String username) {
    mUsername = username;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public String getActivityType() {
    return mActivityType;
  }

  public void setActivityType(String activityType) {
    mActivityType = activityType;
  }

  public LocalDate getTransactionDate() {
    return mTransactionDate;
  }

  public void setTransactionDate(LocalDate transactionDate) {
    mTransactionDate = transactionDate;
  }

  public int getPoints() {
    return mPoints;
  }

  public void setPoints(int points) {
    mPoints = points;
  }

  @Override
  public String toString() {
    return "UserLog{" +
        "mLogID=" + mLogID +
        ", mItemID=" + mItemID +
        ", mUserID=" + mUserID +
        ", mUsername='" + mUsername + '\'' +
        ", mDescription='" + mDescription + '\'' +
        ", mActivityType='" + mActivityType + '\'' +
        ", mTransactionDate=" + mTransactionDate +
        ", mPoints=" + mPoints +
        '}';
  }
}
