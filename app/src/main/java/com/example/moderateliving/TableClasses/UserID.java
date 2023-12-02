package com.example.moderateliving.TableClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.LoggedInToken;

import java.util.Objects;

@Entity(tableName = AppDataBase.USERID_TABLE)
public class UserID {

  @PrimaryKey(autoGenerate = true)
  private int mUserID;

  private String mUsername;

  private String mName;

  private int mPoints;
  private String mPassword;
  private Boolean mIsAdmin;
  private double mWeight;
  private String mBirthday;

  public UserID(int userID, String username, String name, int points, String password, Boolean isAdmin, double weight, String birthday) {
    mUserID = userID;
    mUsername = username;
    mName = name;
    mPoints = points;
    mPassword = password;
    mIsAdmin = isAdmin;
    mWeight = weight;
    mBirthday = birthday;
  }

  public int getUserID() {
    return mUserID;
  }

  public void setUserID(int userID) {
    mUserID = userID;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

  public Boolean getIsAdmin() {
    return mIsAdmin;
  }

  public void setIsAdmin(Boolean isadmin) {
    mIsAdmin = isadmin;
  }

  public double getWeight() {
    return mWeight;
  }

  public void setWeight(double weight) {
    mWeight = weight;
  }

  public String getBirthday() {
    return mBirthday;
  }

  public void setBirthday(String birthday) {
    mBirthday = birthday;
  }

  public int getPoints() {
    return mPoints;
  }

  public void setPoints(int points) {
    mPoints = points;
  }

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(String username) {
    mUsername = username;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public int getHashPassword() {
    return Objects.hash(mUserID, mPassword);
  }

  @Override
  public String toString() {
    return "UserID{" +
        "mUserID=" + mUserID +
        ", mPoints='" + mPoints + '\'' +
        ", mPassword='" + mPassword + '\'' +
        ", mIsAdmin=" + mIsAdmin +
        ", mWeight=" + mWeight +
        ", mBirthday='" + mBirthday + '\'' +
        '}';
  }
}
