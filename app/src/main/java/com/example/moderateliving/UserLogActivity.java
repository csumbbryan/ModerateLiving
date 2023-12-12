package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.LogRecyclerViewInterface;
import com.example.moderateliving.TableClasses.UserLog;
import com.example.moderateliving.databinding.ActivityHealthBinding;
import com.example.moderateliving.databinding.ActivityUserLogBinding;

import java.util.List;

public class UserLogActivity extends AppCompatActivity implements LogRecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.UserLogActivity_USER_ID";
  private static final int NO_USER = 0;
  private static final int LOGOUT_USER = -1;
  private static final String TAG = "User Log Activity";

  private ActivityUserLogBinding mUserLogBinding;
  private ModerateLivingDAO mModerateLivingDAO;
  private RecyclerView recyclerView;
  private LogRecyclerAdapter mLogRecyclerAdapter;
  private Button mButtonUserLogReturnHome;
  private int mLoggedInUserID;

  public static Intent intentFactory(Context packageContext, int mLoggedInUserID) {
    Intent intent = new Intent(packageContext, UserLogActivity.class);
    intent.putExtra(USER_ID, mLoggedInUserID);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_log);

    mUserLogBinding = ActivityUserLogBinding.inflate(getLayoutInflater());
    setContentView(mUserLogBinding.getRoot());

    getDatabase();
    checkUserLoggedIn();
    populateEntries();

    mButtonUserLogReturnHome = mUserLogBinding.buttonUserLogReturnHome;

    mButtonUserLogReturnHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity(mModerateLivingDAO.getUserByID(mLoggedInUserID).getHashPassword());
      }
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    finish();
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
          returnToMainActivity(LOGOUT_USER);
        }
      }.start();
    }
  }

  private void returnToMainActivity(int userPassHash) {
    Intent intent = MainActivity.intentFactory(getApplicationContext(), userPassHash);
    Log.d(TAG, "Returning to Main Activity");
    startActivity(intent);
  }

  private void populateEntries() {
    List<UserLog> userLogs = mModerateLivingDAO.getUserLogByUserID(mLoggedInUserID);
    recyclerView = findViewById(R.id.recyclerViewUserLog);
    if (userLogs != null) {
      mLogRecyclerAdapter = new LogRecyclerAdapter(this, userLogs, this);
      recyclerView.setAdapter(mLogRecyclerAdapter);
    }
  }

  @Override
  public void clickToSelect(int position) {

  }

  @Override
  public void longClickToSelect(int position) {

  }
}