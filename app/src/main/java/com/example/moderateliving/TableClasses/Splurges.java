package com.example.moderateliving.TableClasses;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.ModerateLivingEntries;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Splurges table entry Java object representation
 */
@Entity(tableName = AppDataBase.SPLURGES_TABLE,
    foreignKeys = {@ForeignKey(entity = UserID.class,
        parentColumns = "mUserID",
        childColumns = "mUserID",
        onDelete = ForeignKey.CASCADE)
    })
public class Splurges implements ModerateLivingEntries {
  @PrimaryKey //(autoGenerate = true)
  private int mSplurgeID;
  private int mUserID;
  private String mSplurgeName;
  private String mSplurgeDescription;
  private int mPointsCost;

  public Splurges(int splurgeID, int userID, String splurgeName, String splurgeDescription, int pointsCost) {
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

  public int getUserID() {
    return mUserID;
  }

  @Override
  public boolean isComplete() {
    return false;
  }

  public void setUserID(int userID) {
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

  @Override
  public String getEntryName() {
    return this.mSplurgeName;
  }

  @Override
  public String getDescription() {
    return this.mSplurgeDescription;
  }

  @Override
  public int getPoints() {
    return this.mPointsCost;
  }

  @Override
  public int getID() {
    return this.mSplurgeID;
  }

  public Splurges copy(int splurgeID) {
    Splurges splurge = new Splurges(
        splurgeID,
        this.mUserID,
        this.mSplurgeName,
        this.mSplurgeDescription,
        this.mPointsCost);
    return splurge;
  }
}
