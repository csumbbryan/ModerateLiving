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
        @ForeignKey(entity = Splurges.class,
            parentColumns = "mSplurgeID",
            childColumns = "mSplurgeID",
            onDelete = ForeignKey.CASCADE)
    })
public class SplurgeLog {

  @PrimaryKey(autoGenerate = true)
  private int mSplurgeLogID;

  private int mSplurgeID;
  private int mUserID;
  private String mSplurgeCreateDate;
  private String mSplurgeRedeemDate;
  private boolean mSplurgeIsRedeemed;
  private int mPointsRedeemed;

  public SplurgeLog(int splurgeLogID, int splurgeID, int userID, String splurgeCreateDate,
                    String splurgeRedeemDate, boolean splurgeIsRedeemed, int pointsRedeemed) {
    mSplurgeLogID = splurgeLogID;
    mSplurgeID = splurgeID;
    mUserID = userID;
    mSplurgeCreateDate = splurgeCreateDate;
    mSplurgeRedeemDate = splurgeRedeemDate;
    mSplurgeIsRedeemed = splurgeIsRedeemed;
    mPointsRedeemed = pointsRedeemed;
  }

  public int getSplurgeLogID() {
    return mSplurgeLogID;
  }

  public void setSplurgeLogID(int splurgeLogID) {
    mSplurgeLogID = splurgeLogID;
  }

  public int getSplurgeID() {
    return mSplurgeID;
  }

  public void setSplurgeID(int splurgeID) {
    mSplurgeID = splurgeID;
  }

  public int getUserID() {
    return mUserID;
  }

  public void setUserID(int userID) {
    mUserID = userID;
  }

  public String getSplurgeCreateDate() {
    return mSplurgeCreateDate;
  }

  public void setSplurgeCreateDate(String splurgeCreateDate) {
    mSplurgeCreateDate = splurgeCreateDate;
  }

  public String getSplurgeRedeemDate() {
    return mSplurgeRedeemDate;
  }

  public void setSplurgeRedeemDate(String splurgeRedeemDate) {
    mSplurgeRedeemDate = splurgeRedeemDate;
  }

  public boolean isSplurgeIsRedeemed() {
    return mSplurgeIsRedeemed;
  }

  public void setSplurgeIsRedeemed(boolean splurgeIsRedeemed) {
    mSplurgeIsRedeemed = splurgeIsRedeemed;
  }

  public int getPointsRedeemed() {
    return mPointsRedeemed;
  }

  public void setPointsRedeemed(int pointsRedeemed) {
    mPointsRedeemed = pointsRedeemed;
  }

  @Override
  public String toString() {
    return "SplurgeLog{" +
        "mSplurgeLogID=" + mSplurgeLogID +
        ", mSplurgeID=" + mSplurgeID +
        ", mUserID=" + mUserID +
        ", mSplurgeCreateDate='" + mSplurgeCreateDate + '\'' +
        ", mSplurgeRedeemDate='" + mSplurgeRedeemDate + '\'' +
        ", mSplurgeIsRedeemed=" + mSplurgeIsRedeemed +
        ", mPointsRedeemed=" + mPointsRedeemed +
        '}';
  }
}
