package com.example.moderateliving.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.Splurges;

import java.util.List;

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
  void insert(Splurges... splurges);

  @Update
  void update(Splurges... splurges);

  @Delete
  void delete(Splurges splurge);

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE)
  List<UserID> getUserIDs();

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE + " WHERE mUserID = :userID")
  UserID getUserByID(int userID);

  @Query("SELECT * FROM " + AppDataBase.USERID_TABLE + " WHERE mUsername = :username")
  UserID getUserByUsername(String username);

}
