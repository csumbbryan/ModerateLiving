package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
@Entity(tableName = AppDataBase.SPLURGES_TABLE,
    foreignKeys = {@ForeignKey(entity = UserID.class,
        parentColumns = "mUserID",
        childColumns = "mUserID",
        onDelete = ForeignKey.CASCADE)
    })
public class Splurges {
  @PrimaryKey (autoGenerate = true)
  private int mSplurgeID;
  private String mUserID;
  private String mSplurgeName;
  private String mSplurgeDescription;
  private int mPointsCost;

  public Splurges(int splurgeID, String userID, String splurgeName, String splurgeDescription, int pointsCost) {
    mSplurgeID = splurgeID;
    mUserID = userID;
    mSplurgeName = splurgeName;
    mSplurgeDescription = splurgeDescription;
    mPointsCost = pointsCost;
  }

  public int getSplurgeID() {
    return mSplurgeID;
  }

  public void setSplurgeID(int splurgeID) {
    mSplurgeID = splurgeID;
  }

  public String getUserID() {
    return mUserID;
  }

  public void setUserID(String userID) {
    mUserID = userID;
  }

  public String getSplurgeName() {
    return mSplurgeName;
  }

  public void setSplurgeName(String splurgeName) {
    mSplurgeName = splurgeName;
  }

  public String getSplurgeDescription() {
    return mSplurgeDescription;
  }

  public void setSplurgeDescription(String splurgeDescription) {
    mSplurgeDescription = splurgeDescription;
  }

  public int getPointsCost() {
    return mPointsCost;
  }

  public void setPointsCost(int pointsCost) {
    mPointsCost = pointsCost;
  }

  @Override
  public String toString() {
    return "Splurges{" +
        "mSplurgeID=" + mSplurgeID +
        ", mUserID='" + mUserID + '\'' +
        ", mSplurgeName='" + mSplurgeName + '\'' +
        ", mSplurgeDescription='" + mSplurgeDescription + '\'' +
        ", mPointsCost=" + mPointsCost +
        '}';
  }
}
