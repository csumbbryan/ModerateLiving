package com.example.moderateliving;

import com.example.moderateliving.TableClasses.UserID;

public class LoggedInToken {

  private static LoggedInToken token;
  public static int loggedInUserHash; //CONSIDER MOVING TO METHOD
  private static int userID;
  private static String userName;
  private static boolean isAdmin;
  private static int pointsBalance;
  private static String birthday;
  private static double weight;

  public static int logUserIN(UserID userToBeLoggedIn) {
    if(token == null) {
      token = new LoggedInToken();
    }
    loggedInUserHash = userToBeLoggedIn.getHashPassword();
    userID = userToBeLoggedIn.getUserID();
    userName = userToBeLoggedIn.getName();
    isAdmin = userToBeLoggedIn.getIsAdmin();
    pointsBalance = userToBeLoggedIn.getPoints();
    birthday = userToBeLoggedIn.getBirthday();
    weight = userToBeLoggedIn.getWeight();
    return loggedInUserHash;
  }

  public static boolean logUserOut() {
    token = null;
    if(token == null) {
      return true;
    }
    return false;
  }

  public static LoggedInToken getLoggedInToken() {
    if (token == null) {
      return null;
    }
    return token;
  }

  public static String getUserName() {
    return userName;
  }

  public static int getUserID() {
    return userID;
  }

  public static boolean getIsAdmin() {
    return isAdmin;
  }

  public static int getPointsBalance() {
    return pointsBalance;
  }

  public static String getBirthday() {
    return birthday;
  }

  public static double getWeight() {
    return weight;
  }

  @Override
  public String toString() {
    return userName;
  }
}
