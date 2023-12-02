package com.example.moderateliving;

import androidx.room.Room;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;

import java.util.List;

public class Util {
  private static List<UserID> mUserIDList;


  public static boolean verifyCredentials(List<UserID> mUserIDList, int userID, String password) {
    System.out.println("Made it to verifying credentials.");
    for(UserID user : mUserIDList) {
      if(userID == user.getUserID() && password.equals(user.getPassword())) {
        LoggedInToken.logUserIN(user);
        return true;
      }
    }
    return false;
  }

  public static boolean verifyCredentials(List<UserID> mUserIDList, String username, String password) {
    System.out.println("Made it to verifying credentials.");
    for(UserID user : mUserIDList) {
      if(username.equals(user.getUsername()) && password.equals(user.getPassword())) {
        LoggedInToken.logUserIN(user);
        return true;
      }
    }
    return false;
  }

}
