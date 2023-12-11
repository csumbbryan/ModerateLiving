package com.example.moderateliving;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.room.Room;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;

import java.util.List;

//TODO: verify this can/should be abstract
public abstract class Util {
  private static List<UserID> mUserIDList;

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
}
