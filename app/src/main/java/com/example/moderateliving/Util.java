package com.example.moderateliving;

import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;

import java.util.List;

public class Util {
  private static List<UserID> mUserIDList;
  private static ModerateLivingDAO mModerateLivingDAO;

  public static boolean verifyCredentials(int userID, String password) {
    mUserIDList = mModerateLivingDAO.getUserIDs();
    for(UserID user : mUserIDList) {
      if(userID == user.getUserID() && password == user.getPassword()) {
        return true;
      }
    }
    return false;
  }

  public static boolean verifyCredentials(String username, String password) {
    mUserIDList = mModerateLivingDAO.getUserIDs();
    for(UserID user : mUserIDList) {
      if(username == user.getUsername() && password == user.getPassword()) {
        return true;
      }
    }
    return false;
  }

}
