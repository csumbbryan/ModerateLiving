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
  void update(SplurgeLog splurgeLog);

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE)
  List<UserID> getUserIDs();

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE + " WHERE mUserID = :userID")
  UserID getUserByID(int userID);

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE + " WHERE mUsername = :username")
  UserID getUserByUsername(String username);

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE)
  List<HealthActivities> getHealthActivities();

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE + " WHERE mActivityID = :activityID")
  List<HealthActivities> getHealthActivitiesByID(int activityID);

  @Query("SELECT * FROM " + AppDataBase.HEALTHACTIVITIES_TABLE + " WHERE mUserID = :userID")
  List<HealthActivities> getHealthActivitiesByUser(int userID);

}
