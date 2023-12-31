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

import com.example.moderateliving.AndroidActivity.AdminActivity;
import com.example.moderateliving.AndroidActivity.HealthActivity;
import com.example.moderateliving.AndroidActivity.LoginActivity;
import com.example.moderateliving.AndroidActivity.SignUpActivity;
import com.example.moderateliving.AndroidActivity.SplurgeActivity;
import com.example.moderateliving.AndroidActivity.UserLogActivity;
import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityMainBinding;

import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: driver activity.
 * Provides access to HealthActivity, SplurgeActivity, UserLog, and EditSettings
 * Allows access to Admin Tools if user isAdmin
 */
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

  UserID mLoggedInUser = null;

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

    //userHash = getIntent().getIntExtra(USER_PASSWORD_HASH, 0);
    mWelcomeUserMessage = binding.mainActivityWelcomeText;
    mPointsBalanceCount = binding.textViewPointBalanceDisplay;
    mHealthActivitiesSelect = binding.buttonHealthActivitiesSelect;
    mSplurgesSelect = binding.buttonSplurgesSelect;
    mViewLogSelect = binding.buttonViewLogSelect;
    mAdminToolsSelect = binding.buttonEnterAdminToolsSelect;
    mEditUserSettingsSelect = binding.buttonEditUserSettingsSelect;
    mButtonMainClose = binding.buttonMainClose;

    //mModerateLivingDAO = AppDataBase.getInstance(this).getModerateLivingDAO();

    getDatabase();

    if(!checkUserStatus(mModerateLivingDAO)) {
      Intent intent = LoginActivity.intentFactory(getApplicationContext(), 0); //Update userHash
      Log.d(TAG, "Switching to LoginActivity View");
      startActivity(intent);
      finish();
    }

    if(userHash == -1) {
      finish();
    }

    /**
     * initialize logged in user variables and setup environment based on user values.
     */
    if(mLoggedInUser != null) {
      refreshDisplay();
      Log.d(TAG, "User " + mLoggedInUser.getName() + " is logged in and isAdmin is " + mLoggedInUser.getIsAdmin());
    }

    Log.d(TAG, "Main Activity Started and the following variables are set: "
        + '\n' + "Welcome User Message: " + mWelcomeUserMessage.getText()
        + '\n' + "Points Balance Count: " + mPointsBalanceCount.getText());

    mHealthActivitiesSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = HealthActivity.intentFactory(MainActivity.this, mLoggedInUser.getUserID()); //Update userHash
        Log.d(TAG, "Switching to HealthActivity View");
        startActivity(intent);
      }
    });


    mSplurgesSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = SplurgeActivity.intentFactory(getApplicationContext(), mLoggedInUser.getUserID());
        Log.d(TAG, "Switching to SplurgeActivity View");
        startActivity(intent);
      }
    });

    mViewLogSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = UserLogActivity.intentFactory(getApplicationContext(), mLoggedInUser.getUserID(), false);
        Log.d(TAG, "Switching to User Log Activity View");
        startActivity(intent);
      }
    });

    mAdminToolsSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = AdminActivity.intentFactory(getApplicationContext(), mLoggedInUser.getUserID()); //Update userHash
        Log.d(TAG, "Switching to AdminActivity View");
        startActivity(intent);
      }
    });

    //TODO: feature enhancement: determine if other actions should be taken during MainActivity Close. Separate Method?
    mButtonMainClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        logOutUser();
      }
    });

    mEditUserSettingsSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = SignUpActivity.intentFactory(getApplicationContext(), mLoggedInUser.getUserID());
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    mLoggedInUser = mModerateLivingDAO.getUserByID(mLoggedInUser.getUserID());
    refreshDisplay();
  }

  private void logOutUser() {
    SharedPreferences loginSharedPref = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
    SharedPreferences.Editor loginSharedEditor = loginSharedPref.edit();
    loginSharedEditor.remove(SHARED_PREF_STRING);
    loginSharedEditor.apply();
    mLoggedInUser = null;
    getIntent().putExtra(USER_PASSWORD_HASH, 0);
    //TODO: feature enhancement: update better checking of sharedPreferences being cleared for below log
    Log.d(TAG, "Shared Preferences have been cleared. Closing MainActivity.");
    finish();
  }

  //TODO: feature enhancement: use code returns instead of boolean
  private boolean checkUserStatus(ModerateLivingDAO mModerateLivingDAO) {
    SharedPreferences loginSharedPref = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
    int loginSharedPrefHash = loginSharedPref.getInt(SHARED_PREF_STRING, 0);

    int userPassHash = getIntent().getIntExtra(USER_PASSWORD_HASH, 0);

    mUserIDList = mModerateLivingDAO.getUserIDs();

    if(userPassHash == -1) {
      logOutUser();
    }

    if(userPassHash != 0 ) {
      mLoggedInUser = Util.findUserByHash(mUserIDList,userPassHash);
      if(mLoggedInUser != null) {
        return true;
      } else {
        return false;
      }
    }
    if(loginSharedPrefHash != 0) {
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

  private void refreshDisplay() {
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
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }


}