package com.example.moderateliving;

import com.example.moderateliving.TableClasses.UserID;

public class LoggedInToken {
  public static int loggedInUserHash; //CONSIDER MOVING TO METHOD
  private static int tokenUserID;
  private static String userName;
  private static LoggedInToken token;

  public static int logUserIN(UserID userToBeLoggedIn) {
    if(token == null) {
      token = new LoggedInToken();
    }
    loggedInUserHash = userToBeLoggedIn.getHashPassword();
    tokenUserID = userToBeLoggedIn.getUserID();
    userName = userToBeLoggedIn.getName();
    return loggedInUserHash;
  }

  public static LoggedInToken getLoggedInToken() {
    if (token == null) {
      return null;
    }
    return token;
  }

  @Override
  public String toString() {
    return userName;
  }
}
