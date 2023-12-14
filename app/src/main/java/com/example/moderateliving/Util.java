package com.example.moderateliving;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.HealthActivityLog;
import com.example.moderateliving.TableClasses.SplurgeLog;
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
  public static final String TYPE_SPLURGE = "Splurge";
  public static final String TYPE_HEALTH_ACTIVITY = "Health Activity";
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

  public static boolean createEntryLog(Context context, int userID, String description, ModerateLivingEntries activityEntry) {
    getDatabase(context);
    LocalDate date = LocalDate.now();
    String activityType;
    if(activityEntry instanceof HealthActivities) {
      activityType = TYPE_HEALTH_ACTIVITY;
      //TODO: If statement may not be needed as this should be handled in Activity Class
      if (description.equals(LOG_CREATED)) {
        int activityLogID = mModerateLivingDAO.getMaxHealthActivityLogID() + 1;
        HealthActivityLog healthActivityLog = new HealthActivityLog(
            activityLogID,
            activityEntry.getID(),
            activityEntry.getUserID(),
            date,
            null,
            false,
            activityEntry.getPoints()
        );
        mModerateLivingDAO.insert(healthActivityLog);
        logToUserLog(context, userID, Util.LOG_CREATED, healthActivityLog); //TODO: need to manually set primary key
      }
    } else if (activityEntry instanceof Splurges) {
      activityType = TYPE_SPLURGE;
      //TODO: If statement may not be needed as this should be handled in Activity Class
      if (description.equals(LOG_CREATED) || description.equals(LOG_REDEEMED)) {
        int splurgeLogID = mModerateLivingDAO.getMaxSplurgeLogID() + 1;
        SplurgeLog splurgeLog = new SplurgeLog(
            splurgeLogID,
            activityEntry.getID(),
            activityEntry.getUserID(),
            date,
            null,
            false,
            activityEntry.getPoints()
        );
        mModerateLivingDAO.insert(splurgeLog);
        logToUserLog(context, userID, description, splurgeLog); //TODO: need to manually set primary key
      }
    }
    return true;
  }

  //TODO: Decouple logToUserLog from ActivityLog and SplurgeLog
  //TODO: Refactor to use logID instead?
  public static boolean logToUserLog(Context context, int userID, String description, LogInterface logEntry) {
    getDatabase(context);
    LocalDate date = LocalDate.now();
    String entryName = "";
    String activityType = "";
    if(logEntry instanceof HealthActivityLog) {
      activityType = Util.TYPE_HEALTH_ACTIVITY;
      entryName = mModerateLivingDAO.getHealthActivitiesByID(logEntry.getEntryID()).getEntryName();
      if (description.equals(LOG_COMPLETED)) {
        HealthActivityLog healthActivityLog = mModerateLivingDAO.getHealthActivityLogByLogID(logEntry.getLogID());
        healthActivityLog.setDateCompleted(date);
        healthActivityLog.setComplete(true);
        mModerateLivingDAO.update(healthActivityLog);
      }
    } else if (logEntry instanceof SplurgeLog) {
      activityType = Util.TYPE_SPLURGE;
      entryName = mModerateLivingDAO.getSplurgeByID(logEntry.getEntryID()).getEntryName();
      if (description.equals(LOG_REDEEMED)){
        SplurgeLog splurgeLog = mModerateLivingDAO.getSplurgeLogByLogID(logEntry.getLogID());
        splurgeLog.setSplurgeRedeemDate(date);
        splurgeLog.setSplurgeIsRedeemed(true);
        mModerateLivingDAO.update(splurgeLog);
      }
    } else {
      //TODO: Stuff to do if no types match
    }
    UserLog userLog = new UserLog(
        userID,
        logEntry.getLogID(),
        entryName,
        mModerateLivingDAO.getUserByID(userID).getUsername(),
        description,
        activityType,
        date,
        logEntry.getPoints()); //TODO: Review userLog for proper instantiation
    mModerateLivingDAO.insert(userLog);
    return true;
  }

  public static boolean undoRedeem(Context context, int userID, SplurgeLog splurgeLog) {
    //TODO: Splurge Log should be deleted because each splurge redeem has a separate log
    getDatabase(context);
    if(splurgeLog == null) {
      Toast.makeText(context, "Splurge Log does not exist. Splurge may have been deleted.", Toast.LENGTH_LONG).show();
      return false;
    } else {
      UserID user = mModerateLivingDAO.getUserByID(userID);
      int userPointsRedeemed = splurgeLog.getPointsRedeemed();
      int userPointsCurrent = mModerateLivingDAO.getUserByID(userID).getPoints();
      userPointsCurrent += userPointsRedeemed;
      user.setPoints(userPointsCurrent);
      mModerateLivingDAO.delete(splurgeLog);
      mModerateLivingDAO.update(user);
      return true;
    }
  }

  public static boolean undoCreateSplurge(Context context, int userID, SplurgeLog splurgeLog) {
    getDatabase(context);
    if(splurgeLog == null) {
      Toast.makeText(context, "Splurge Log does not exist. Splurge may have been deleted.", Toast.LENGTH_LONG).show();
      return false;
    } else if (splurgeLog.isSplurgeIsRedeemed() || splurgeLog.getSplurgeID() == null) {
      Toast.makeText(context, "Splurge appears to have been redeemed. Action cannot be undone.", Toast.LENGTH_LONG).show();
      return false;
    } else {
      Splurges splurge = mModerateLivingDAO.getSplurgeByID(splurgeLog.getSplurgeID());
      mModerateLivingDAO.delete(splurge);
      mModerateLivingDAO.delete(splurgeLog);
      return true;
    }
  }

  public static boolean undoCompleteHealthActivity(Context context, int userID, HealthActivityLog healthActivityLog) {
    //TODO: Setup condition for null log entry
    getDatabase(context);
    if(healthActivityLog == null) {
      Toast.makeText(context, "Health Activity Log does not exist. Health Activity may have been deleted.", Toast.LENGTH_LONG).show();
      return false;
    } else {
      int userPointsCompleted = healthActivityLog.getPointsEarned();
      int userPointsCurrent = mModerateLivingDAO.getUserByID(userID).getPoints();
      HealthActivities healthActivity = mModerateLivingDAO.getHealthActivitiesByID(healthActivityLog.getActivityID());
      if (userPointsCurrent >= userPointsCompleted) {
        UserID user = mModerateLivingDAO.getUserByID(userID);
        userPointsCurrent -= userPointsCompleted;
        healthActivityLog.setComplete(false);
        healthActivityLog.setDateCompleted(null);
        healthActivity.setIsComplete(false);
        mModerateLivingDAO.update(healthActivity);
        user.setPoints(userPointsCurrent);
        mModerateLivingDAO.update(user);
        mModerateLivingDAO.update(healthActivityLog);
        return true;
      } else {
        Toast.makeText(context, "You do not have enough points to undo this activity. Undo Splurge redemption or earn more Health Activity points!", Toast.LENGTH_LONG).show();
        return false;
      }
    }
  }

  public static boolean undoCreateHealthActivity(Context context, int userID, HealthActivityLog healthActivityLog) {
    getDatabase(context);
    if(healthActivityLog == null) {
      Toast.makeText(context, "Health Activity Log does not exist. Health Activity may have been deleted.", Toast.LENGTH_LONG).show();
      return false;
    } else if (healthActivityLog.isComplete() || healthActivityLog.getActivityID() == null) {
      Toast.makeText(context, "Health Activity appears to have been completed. Action cannot be undone.", Toast.LENGTH_LONG).show();
      return false;
    } else {
      HealthActivities healthActivity = mModerateLivingDAO.getHealthActivitiesByID(healthActivityLog.getActivityID());
      mModerateLivingDAO.delete(healthActivity);
      mModerateLivingDAO.delete(healthActivityLog);
      return true;
    }
  }
}
