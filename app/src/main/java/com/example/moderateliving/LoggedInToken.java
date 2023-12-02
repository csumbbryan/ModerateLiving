package com.example.moderateliving;

import com.example.moderateliving.TableClasses.UserID;

public class LoggedInToken {
  public static int loggedInUserHash; //CONSIDER MOVING TO METHOD
  public static int userID;
  private static LoggedInToken token;

  public int logUserIN(UserID userToBeLoggedIn) {
    LoggedInToken token = getLoggedInToken();
    token.loggedInUserHash = userToBeLoggedIn.getHashPassword();
    token.userID = userToBeLoggedIn.getUserID();
    return loggedInUserHash;
  }

  private LoggedInToken getLoggedInToken() {
    if (token == null) {
      token = new LoggedInToken();
    }
    return token;
  }
}
