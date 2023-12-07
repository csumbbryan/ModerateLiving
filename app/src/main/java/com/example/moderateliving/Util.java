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

public class Util {
  private static List<UserID> mUserIDList;

  public static boolean verifyCredentials(List<UserID> mUserIDList, int userHash) {
    System.out.println("Made it to verifying credentials.");
    for(UserID user : mUserIDList) {
      if(userHash == user.getHashPassword()) {
        //LoggedInToken.logUserIN(user); //TODO: review for LoggedInToken need
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

  /* TODO: Return to this -- startActivities from non-activity
  public static void logOutUser(Context context) {
    Intent intent = MainActivity.intentFactory(context, 0);
    startActivity(context, intent, new Bundle());
  }*/
}
