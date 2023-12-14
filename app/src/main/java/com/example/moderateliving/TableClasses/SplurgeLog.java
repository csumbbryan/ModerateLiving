package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.LogInterface;

import java.time.LocalDate;

@Entity(tableName = AppDataBase.SPLURGES_LOG_TABLE,
    foreignKeys = {
      @ForeignKey(entity = UserID.class, parentColumns = "mUserID", childColumns = "mUserID", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Splurges.class,
            parentColumns = "mSplurgeID",
            childColumns = "mSplurgeID",
            onDelete = ForeignKey.CASCADE
        )
    })
public class SplurgeLog implements LogInterface {

  @PrimaryKey(autoGenerate = true)
  private int mSplurgeLogID;

  private Integer mSplurgeID;
  private int mUserID;
  private LocalDate mSplurgeCreateDate;
  private LocalDate mSplurgeRedeemDate;
  private boolean mSplurgeIsRedeemed;
  private int mPointsRedeemed;

  public SplurgeLog(int splurgeLogID, int splurgeID, int userID, LocalDate splurgeCreateDate,
                    LocalDate splurgeRedeemDate, boolean splurgeIsRedeemed, int pointsRedeemed) {
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

  public Integer getSplurgeID() {
    return mSplurgeID;
  }

  public void setSplurgeID(int splurgeID) {
    mSplurgeID = splurgeID;
  }


  @Override
  public Integer getEntryID() {
    return getSplurgeID();
  }

  @Override
  public int getLogID() {
    return getSplurgeLogID();
  }

  @Override
  public LocalDate getCreateDate() {
    return getSplurgeCreateDate();
  }

  @Override
  public LocalDate getCompleteDate() {
    return getSplurgeRedeemDate();
  }

  @Override
  public int getPoints() {
    return getPointsRedeemed();
  }

  public int getUserID() {
    return mUserID;
  }
  public void setUserID(int userID) {
    mUserID = userID;
  }

  public LocalDate getSplurgeCreateDate() {
    return mSplurgeCreateDate;
  }

  public void setSplurgeCreateDate(LocalDate splurgeCreateDate) {
    mSplurgeCreateDate = splurgeCreateDate;
  }

  public LocalDate getSplurgeRedeemDate() {
    return mSplurgeRedeemDate;
  }

  public void setSplurgeRedeemDate(LocalDate splurgeRedeemDate) {
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
