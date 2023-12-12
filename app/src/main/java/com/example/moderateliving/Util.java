package com.example.moderateliving;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.room.Room;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.Splurges;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.TableClasses.UserLog;

import java.time.LocalDate;
import java.util.List;

//TODO: verify this can/should be abstract
public abstract class Util {
  public static final String LOG_CREATED = "created";
  public static final String LOG_COMPLETED = "completed";
  public static final String LOG_DELETED = "deleted";
  public static final String LOG_REDEEMED = "redeemed";
  public static final String LOG_UPDATED = "updated";
  private static List<UserID> mUserIDList;
  private static ModerateLivingDAO mModerateLivingDAO;

  private static void getDatabase(Context context) {
    mModerateLivingDAO = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  public static boolean verifyCredentials(List<UserID> mUserIDList, int userHash) {
    for(UserID user : mUserIDList) {
      if(userHash == user.getHashPassword()) {
        return true;
      }
    }
    return false;
  }

  public static UserID findUserByHash(List<UserID> mUserIDList, int userHash) {
    UserID userID = null;
    for(UserID userIDList : mUserIDList) {
      if(userHash == userIDList.getHashPassword()) {
        userID = userIDList;
      }
    }
    return userID;
  }

  //TODO: This works! startActivities from non-activity -- stitch into other activities for logoff
  public static void logOutUser(Context context) {
    Intent intent = MainActivity.intentFactory(context, -1);
    startActivity(context, intent, new Bundle());
  }

  public static boolean logToUserLog(Context context, int userID, String description, ModerateLivingEntries activityEntry) {
    getDatabase(context);
    LocalDate date = LocalDate.now(); //TODO: update format for LocalDate
    String activityType;
    if(activityEntry instanceof HealthActivities) {
      activityType = "Health Activity";
    } else if (activityEntry instanceof Splurges) {
      activityType = "Splurge";
    } else {
      activityType = "";
    }
    UserLog userLog = new UserLog(
        userID,
        activityEntry.getID(),
        mModerateLivingDAO.getUserByID(userID).getUsername(),
        description,
        activityType,
        date,
        activityEntry.getPoints()); //TODO: Review userLog for proper instantiation
    mModerateLivingDAO.insert(userLog);
    return true;
  }
}
