package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
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
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityHealthBinding;

import java.util.List;

//TODO: add delete and logout button
//TODO: add functionality to limit health-activity creation for unique names
//TODO: move activities to activity log??
public class HealthActivity extends AppCompatActivity implements RecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.HealthActivity_USER_ID";
  private static final String TAG = "HealthActivity";
  private static final int LOGOUT_USER = -1;
  private static final int NO_USER = 0;
  RecyclerView recyclerView;
  HealthRecyclerAdapter mHealthRecyclerAdapter;

  ActivityHealthBinding mHealthActivityBinding;
  ModerateLivingDAO mModerateLivingDAO;
  Button mButtonHealthActivityHome;
  Button mButtonHealthActivityCreate;
  List<HealthActivities> mHealthActivities;
  int mLoggedInUserID = NO_USER;

  public static Intent intentFactory(Context packageContext, int mLoggedInUserID) {
    Intent intent = new Intent(packageContext, HealthActivity.class);
    intent.putExtra(USER_ID, mLoggedInUserID);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health);

    mHealthActivityBinding = ActivityHealthBinding.inflate(getLayoutInflater());
    setContentView(mHealthActivityBinding.getRoot());

    getDatabase();
    checkUserLoggedIn();
    populateEntries();

    mButtonHealthActivityHome = mHealthActivityBinding.buttonHealthActivityHome;
    mButtonHealthActivityCreate = mHealthActivityBinding.buttonHealthActivityCreate;

    mButtonHealthActivityHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity(mModerateLivingDAO.getUserByID(mLoggedInUserID).getHashPassword());
      }
    });

    mButtonHealthActivityCreate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int activityID = 0;
        Intent intent = HealthConfigActivity.intentFactory(getApplicationContext(), activityID, mLoggedInUserID);
        startActivity(intent);
      }
    });
  }

  private void returnToMainActivity(int userPassHash) {
    Intent intent = MainActivity.intentFactory(getApplicationContext(), userPassHash);
    Log.d(TAG, "Returning to Main Activity");
    startActivity(intent);
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

  private void populateEntries() {
    recyclerView = findViewById(R.id.recyclerViewHealth);
    mHealthActivities = mModerateLivingDAO.getHealthActivitiesByUser(mLoggedInUserID);
    mHealthRecyclerAdapter = new HealthRecyclerAdapter(this, mHealthActivities, this);
    recyclerView.setAdapter(mHealthRecyclerAdapter);
  }

  private void checkUserLoggedIn() {
    mLoggedInUserID = getIntent().getIntExtra(USER_ID, NO_USER);
    if(mLoggedInUserID == 0) {
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

  @Override
  public void onCheckBoxSelect(HealthActivities healthActivity, boolean recreate) {
    int completedPoints = healthActivity.getActivityPoints();
    UserID user = mModerateLivingDAO.getUserByID(mLoggedInUserID);
    int userPoints = user.getPoints();
    userPoints = userPoints + completedPoints;
    user.setPoints(userPoints);
    mModerateLivingDAO.update(user);
    mModerateLivingDAO.delete(healthActivity);
    Toast.makeText(getApplicationContext(),
        "Activity completed: " + healthActivity.getActivityName() + " with " + completedPoints + " pts", Toast.LENGTH_LONG).show();

    //TODO: Log to HealthActivityLogs database
    if(recreate){
      HealthActivities newHealthActivities = healthActivity.copy();
      mModerateLivingDAO.insert(newHealthActivities);
      mHealthRecyclerAdapter.updateHealthEntryList(newHealthActivities);
      //TODO: Log to HealthActivityLogs database
    }
  }

  @Override
  public void onEntryLongClick(int mActivityID) {
    Intent intent = HealthConfigActivity.intentFactory(getApplicationContext(), mActivityID, mLoggedInUserID);
    startActivity(intent);
  }
}