package com.example.moderateliving.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.HealthActivityLog;
import com.example.moderateliving.TableClasses.Splurges;
import com.example.moderateliving.TableClasses.SplurgeLog;
import com.example.moderateliving.TableClasses.UserLog;

import java.util.List;

/**
 * @author Bryan Zanoli
 * @since November 26, 2023
 * </p>
 * Abstract:
 * Room Data Access Object used to connect to Room database abstraction layer
 * Includes standard CRUD queries as well as custom retrievers for entries by userID and username
 */
@Dao
public interface ModerateLivingDAO {

  @Insert
  void insert(UserID... userIDS);

  @Update
  void update(UserID... userIDS);

  @Delete
  void delete(UserID userID);

  @Insert
  void insert(HealthActivities... healthActivities);

  @Update
  void update(HealthActivities... healthActivities);

  @Delete
  void delete(HealthActivities healthActivity);

  @Insert
  void insert(HealthActivityLog... healthActivityLogs);

  @Update
  void update(HealthActivityLog... healthActivityLogs);

  @Delete
  void delete(HealthActivityLog healthActivityLog);

  @Insert
  void insert(Splurges... splurges);

  @Update
  void update(Splurges... splurges);

  @Delete
  void delete(Splurges splurge);

  @Insert
  void insert(SplurgeLog... splurgeLogs);

  @Update
  void update(SplurgeLog... splurgeLogs);

  @Delete
  void delete(SplurgeLog splurgeLog);

  @Insert
  void insert(UserLog... userLogs);

  @Update
  void update(UserLog... userLogs);

  @Delete
  void delete(UserLog userLog);

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE)
  List<UserID> getUserIDs();

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE + " WHERE mUserID = :userID")
  UserID getUserByID(int userID);

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE + " WHERE mUsername = :username")
  UserID getUserByUsername(String username);

  @Query("SELECT MAX(mUserID) FROM " + AppDataBase.USERID_TABLE)
  int getMaxUserID();

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE)
  List<HealthActivities> getHealthActivities();

  @Query("SELECT MAX(mActivityID) FROM " + AppDataBase.HEALTHACTIVITIES_TABLE)
  int getMaxHealthActivityID();

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE + " WHERE mActivityID = :activityID")
  HealthActivities getHealthActivitiesByID(int activityID);

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE + " WHERE mUserID = :userID AND mIsComplete = 0 Order by mActivityID Asc")
  List<HealthActivities> getHealthActivitiesByUser(int userID);

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE + " WHERE mUserID = :userID Order by mActivityID Asc")
  List<HealthActivities> getHealthActivitiesByUserAll(int userID);

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_LOG_TABLE)
  List<HealthActivityLog> getHealthActivityLogs();

  @Query("SELECT MAX(mActivityLogID) FROM " + AppDataBase.HEALTHACTIVITIES_LOG_TABLE)
  int getMaxHealthActivityLogID();

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_LOG_TABLE + " WHERE mActivityID = :activityID")
  HealthActivityLog getHealthActivityLogByID(Integer activityID);

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_LOG_TABLE + " WHERE mActivityLogID = :activityLogID")
  HealthActivityLog getHealthActivityLogByLogID(int activityLogID);

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_TABLE)
  List<Splurges> getSplurges();

  @Query("SELECT MAX(mSplurgeID) FROM " + AppDataBase.SPLURGES_TABLE)
  int getMaxSplurgeID();

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_TABLE + " WHERE mUserID = :userID")
  List<Splurges> getSplurgesByUserID(int userID);

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_TABLE + " WHERE mSplurgeID = :splurgeID")
  Splurges getSplurgeByID(int splurgeID);

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_TABLE + " WHERE mSplurgeName = :splurgeName AND mUserID = :userID")
  List<Splurges> getSplurgesByNameAndUserID(String splurgeName, int userID);

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_LOG_TABLE)
  List<SplurgeLog> getSplurgeLogs();

  @Query("SELECT MAX(mSplurgeLogID) FROM " + AppDataBase.SPLURGES_LOG_TABLE)
  int getMaxSplurgeLogID();

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_LOG_TABLE + " WHERE mSplurgeLogID = :splurgeLogID")
  SplurgeLog getSplurgeLogByLogID(int splurgeLogID);

  @Query("SELECT * FROM " + AppDataBase.SPLURGES_LOG_TABLE + " WHERE mSplurgeID = :splurgeID")
  SplurgeLog getSplurgeLogByID(Integer splurgeID);

  @Query("SELECT * FROM " + AppDataBase.USER_LOG_TABLE + " Order by mLogID Desc ")
  List<UserLog> getUserLogs();

  @Query("SELECT * FROM " + AppDataBase.USER_LOG_TABLE + " Where mUserID = :userID Order by mLogID Desc ")
  List<UserLog> getUserLogByUserID(int userID);
}
