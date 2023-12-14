package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

  public static final String TAG = "AdminActivity";
  private static final String SHARED_PREF_STRING = "com.example.moderateliving_SHARED_PREF_STRING";
  private static final String USER_ID = "com.example.moderateliving.AdminActivity_USER_ID";
  private static final int NO_USER = 0;
  private static final int LOGOUT_USER = -1;

  private Button mButtonDatabaseReset;
  private Button mButtonUserManagement;
  private ImageButton mButtonAdminClose;
  private ModerateLivingDAO mModerateLivingDAO;
  private int mLoggedInUserID;

  ActivityAdminBinding mActivityAdminBinding;
  public static Intent intentFactory(Context packageContext, int mUserID) {
    Intent intent = new Intent(packageContext, AdminActivity.class);
    intent.putExtra(USER_ID, mUserID);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    mActivityAdminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
    setContentView(mActivityAdminBinding.getRoot());

    mButtonDatabaseReset = mActivityAdminBinding.buttonAdminActivityDatabaseReset;
    mButtonAdminClose = mActivityAdminBinding.buttonAdminClose;
    mButtonUserManagement = mActivityAdminBinding.buttonAdminActivityUserManagement;

    getDatabase();
    checkUserLoggedIn();

    mButtonAdminClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        returnToMainActivity();
      }
    });

    mButtonUserManagement.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = UserManagementActivity.intentFactory(getApplicationContext(), mLoggedInUserID);
        startActivity(intent);
      }
    });

  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  private void checkUserLoggedIn() {
    mLoggedInUserID = getIntent().getIntExtra(USER_ID, NO_USER);
    if (mLoggedInUserID == 0) {
      Toast notLoggedIn = Toast.makeText(this, "You are not logged in. Exiting the app.", Toast.LENGTH_SHORT);
      notLoggedIn.show();
      new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
          returnToMainActivity();
        }
      }.start();
    }
  }

  private void returnToMainActivity() {
    Log.d(TAG, "Returning to Main Activity");
    finish();
  }
}