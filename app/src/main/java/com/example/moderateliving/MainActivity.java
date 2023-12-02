package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String USER_PASSWORD_HASH =
      "com.example.moderateliving.MainActivity_USER_PASSWORD_HASH";
  private static final String TAG = "MainActivity";

  ActivityMainBinding binding;
  TextView mMainDisplay;
  //Buttons and Text Fields Here
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

  LoggedInToken mLoggedInUser;

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

    mWelcomeUserMessage = binding.mainActivityWelcomeText;
    mPointsBalanceCount = binding.textViewPointsBalance;
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

    //TODO: User Logged In? (Separate Method?)
    if(!checkUserStatus()) {
      Intent intent = LoginActivity.intentFactory(getApplicationContext(), 0); //Update userHash
      startActivity(intent);
    }

    /*
    mHealthActivitiesSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: intent factory and send to Health Activities view

      }
    });

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
    });

    mAdminToolsSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: intent factory and send to Admin Tools view
      }
    });

    mButtonMainClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: set application to logout and exit app
        //CALL: Clear User Credentials
        //CALL: Close App
      }
    });*/

    refreshDisplay();
  }

  //TODO: use code returns instead of boolean
  private boolean checkUserStatus() {
    int userPassHash = getIntent().getIntExtra(USER_PASSWORD_HASH, 0);
    if(mLoggedInUser != null) {
      return true;
    }
    if(userPassHash > 0 ) {
      return true;
    }
    return false;
  }

  //TODO: Update this upon completion of other methods
  private void refreshDisplay() {

  }
}