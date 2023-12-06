package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String USER_PASSWORD_HASH =
      "com.example.moderateliving.MainActivity_USER_PASSWORD_HASH";
  private static final String SHARED_PREF_STRING = "com.example.moderateliving_SHARED_PREF_STRING";
  private static final String TAG = "MainActivity";

  ActivityMainBinding binding;
  TextView mWelcomeUserMessage;
  TextView mPointsBalanceCount;
  Button mHealthActivitiesSelect;
  Button mSplurgesSelect;
  Button mViewLogSelect;
  Button mAdminToolsSelect;
  Button mEditUserSettingsSelect;
  ImageButton mButtonMainClose;

  ModerateLivingDAO mModerateLivingDAO;

  List<UserID> mUserIDList;

  UserID mLoggedInUser = null; //TODO: review if this is necessary

  int userHash = 0;

  public static Intent intentFactory(Context packageContext, Integer userHash) {
    Intent intent = new Intent(packageContext, MainActivity.class);
    intent.putExtra(USER_PASSWORD_HASH, userHash);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    userHash = getIntent().getIntExtra(USER_PASSWORD_HASH, 0);
    mWelcomeUserMessage = binding.mainActivityWelcomeText;
    mPointsBalanceCount = binding.textViewPointBalanceDisplay;
    mHealthActivitiesSelect = binding.buttonHealthActivitiesSelect;
    mSplurgesSelect = binding.buttonSplurgesSelect;
    mViewLogSelect = binding.buttonViewLogSelect;
    mAdminToolsSelect = binding.buttonEnterAdminToolsSelect;
    mEditUserSettingsSelect = binding.buttonEditUserSettingsSelect;
    mButtonMainClose = binding.buttonMainClose;
    Log.d(TAG, "Main Activity Started and the following variables are set: "
    + '\n' + "Welcome User Message: " + mWelcomeUserMessage.getText()
    + '\n' + "Points Balance Count: " + mPointsBalanceCount.getText());

    //mModerateLivingDAO = AppDataBase.getInstance(this).getModerateLivingDAO();

    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();

    if(!checkUserStatus(mModerateLivingDAO)) {
      Intent intent = LoginActivity.intentFactory(getApplicationContext(), 0); //Update userHash
      Log.d(TAG, "Switching to LoginActivity View");
      startActivity(intent);
    }

    if(userHash == -1) {
      finish();
    }

    /**
     * initialize logged in user variables and setup environment based on user values.
     */
    if(mLoggedInUser != null) {
      //int userPoints = LoggedInToken.getPointsBalance(); //TODO: review for LoggedInToken need
      //String userName = LoggedInToken.getUserName(); //TODO: review for LoggedInToken need
      //boolean isAdmin = LoggedInToken.getIsAdmin(); //TODO: review for LoggedInToken need
      int userPoints = mLoggedInUser.getPoints();
      String name = mLoggedInUser.getName();
      boolean isAdmin = mLoggedInUser.getIsAdmin();
      String pointsDisplay = userPoints + (userPoints != 1 ? " Points" : " Point");
      String welcomeDisplay = "Welcome " + name;
      mWelcomeUserMessage.setText(welcomeDisplay);
      mPointsBalanceCount.setText(pointsDisplay);
      if(isAdmin) {
        mAdminToolsSelect.setVisibility(View.VISIBLE);
      }
      Log.d(TAG, "User " + name + " is logged in and isAdmin is " + isAdmin);
    }

    CreateHealthActivities();


    mHealthActivitiesSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: intent factory and send to Health Activities view -- update for passing Extra
        Intent intent = HealthActivity.intentFactory(getApplicationContext()); //Update userHash
        Log.d(TAG, "Switching to HealthActivity View");
        startActivity(intent);
      }
    });

    /*
    mSplurgesSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: intent factory and send to Splurges view
      }
    });

    mViewLogSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: intent factory and send to User Log view
      }
    });*/

    mAdminToolsSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: Update for passing Extra in AdminActivity
        Intent intent = AdminActivity.intentFactory(getApplicationContext()); //Update userHash
        Log.d(TAG, "Switching to AdminActivity View");
        startActivity(intent);
      }
    });

    //TODO: Determine if other actions should be taken during MainActivity Close. Separate Method?
    mButtonMainClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SharedPreferences loginSharedPref = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor loginSharedEditor = loginSharedPref.edit();
        loginSharedEditor.remove(SHARED_PREF_STRING);
        loginSharedEditor.apply();
        //TODO: update better checking of sharedPreferences being cleared for below log
        Log.d(TAG, "Shared Preferences have been cleared. Closing MainActivity.");
        //LoggedInToken.logUserOut(); //TODO: review for LoggedInToken need
        finish();
        //CALL: Close App
      }
    });

    refreshDisplay();
  }

  //TODO: use code returns instead of boolean
  private boolean checkUserStatus(ModerateLivingDAO mModerateLivingDAO) {
    SharedPreferences loginSharedPref = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
    int loginSharedPrefHash = loginSharedPref.getInt(SHARED_PREF_STRING, 0);

    int userPassHash = getIntent().getIntExtra(USER_PASSWORD_HASH, 0);
    mUserIDList = mModerateLivingDAO.getUserIDs();
    //if(LoggedInToken.getLoggedInToken() != null) {return true;} //TODO: review for LoggedInToken need
    if(userPassHash != 0 ) {
      //TODO: build in more checks to ensure success before returning true
      //LoggedInToken.logUserIN(Util.findUserByHash(mUserIDList, userPassHash));
      mLoggedInUser = Util.findUserByHash(mUserIDList,userPassHash);
      if(mLoggedInUser != null) {
        return true;
      } else {
        return false;
      }
    }
    if(loginSharedPrefHash != 0) {
      //TODO: build in more checks to ensure success before returning true
      //LoggedInToken.logUserIN(Util.findUserByHash(mUserIDList, loginSharedPrefHash));
      mLoggedInUser = Util.findUserByHash(mUserIDList,loginSharedPrefHash);
      if(mLoggedInUser != null){
        return true;
      } else {
        return false;
      }
    }
    if(mUserIDList.isEmpty()) {
      UserID user1 = new UserID(
      "testuser1",
      "Bob Ross",
      0,
      "testuser1",
      false,
      178.0,
      "1965-03-25");
      UserID user2 = new UserID(
          "admin2",
          "Tony Stark",
          0,
          "admin2",
          true,
          185.0,
          "1965-04-04"
      );
      mModerateLivingDAO.insert(user1);
      mModerateLivingDAO.insert(user2);
    }
    return false;
  }

  //TODO: Update this upon completion of other methods
  private void refreshDisplay() {

  }

  //TODO: Delete this when no longer needed
  private void CreateHealthActivities() {
    HealthActivities healthActivity = new HealthActivities(
        mLoggedInUser.getUserID(),
        "Running",
        "Running 3 miles every week",
        3,
        false);
    HealthActivities healthActivity1 = new HealthActivities(
        mLoggedInUser.getUserID(),
        "Healthy Weekend Eating",
        "Stay healthy on the weekends!",
        1,
        false);
    HealthActivities healthActivity2 = new HealthActivities(
        mLoggedInUser.getUserID(),
        "Gym",
        "Earn a point for each workout, up to 4 per week",
        1,
        false);
    mModerateLivingDAO.insert(healthActivity);
    mModerateLivingDAO.insert(healthActivity1);
    mModerateLivingDAO.insert(healthActivity2);
  }

}