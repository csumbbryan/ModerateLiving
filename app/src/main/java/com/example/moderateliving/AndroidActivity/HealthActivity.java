package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.EntryRecyclerAdapter;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.ModerateLivingEntries;
import com.example.moderateliving.R;
import com.example.moderateliving.RecyclerViewInterface;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.HealthActivityLog;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivityHealthBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Support creation, review, and completion of "Health Activities" tasks
 */
//TODO: Consider LiveData?
public class HealthActivity extends AppCompatActivity implements RecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.HealthActivity_USER_ID";
  private static final String TAG = "HealthActivity";
  private static final int LOGOUT_USER = -1;
  private static final int NO_USER = 0;
  private static final String LOG_CREATED = "created";
  private static final String LOG_COMPLETED = "completed";
  private RecyclerView recyclerView;
  private EntryRecyclerAdapter mEntryRecyclerAdapter;

  private ActivityHealthBinding mHealthActivityBinding;
  private ModerateLivingDAO mModerateLivingDAO;
  private Button mButtonHealthActivityHome;
  private Button mButtonHealthActivityCreate;
  Button mButtonHealthActivityShowAll;
  private List<HealthActivities> mHealthActivities;

  //TESTING INTERFACE
  private List<ModerateLivingEntries> mLivingEntries = new ArrayList<>();
  private int mLoggedInUserID = NO_USER;
  private boolean mShowAll = false;
  int mSelectedItemPosition = RecyclerView.NO_POSITION;

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
    mButtonHealthActivityShowAll = mHealthActivityBinding.buttonHealthActivityShowAll;

    mButtonHealthActivityHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity();
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

    mButtonHealthActivityShowAll.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!mShowAll) {
          mButtonHealthActivityShowAll.setBackgroundColor(getResources().getColor(R.color.grayLight, null));
          mButtonHealthActivityShowAll.setTextColor(getResources().getColor(R.color.black, null));
        }
        if(mShowAll) {
          mButtonHealthActivityShowAll.setBackgroundColor(getResources().getColor(R.color.grayExtraDark, null));
          mButtonHealthActivityShowAll.setTextColor(getResources().getColor(R.color.white, null));
        }
        mShowAll = !mShowAll;
        populateEntries();
      }
    });
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    //The Easy Way:
    //The hard way:
    mShowAll = false;
    int healthActivityCount = mModerateLivingDAO.getHealthActivitiesByUser(mLoggedInUserID).size();
    if(healthActivityCount > 0 && healthActivityCount > mLivingEntries.size()) {
      int index = mLivingEntries.size();
      List<HealthActivities> newHealthActivities = mModerateLivingDAO.getHealthActivitiesByUser(mLoggedInUserID);
      for(int i = index; i < healthActivityCount; i++) {
        mLivingEntries.add(i, newHealthActivities.get(i));
        mEntryRecyclerAdapter.notifyItemInserted(i);
      }
    }
    if(healthActivityCount > 0 && healthActivityCount < mLivingEntries.size()) {
      mLivingEntries.remove(mSelectedItemPosition);
      mEntryRecyclerAdapter.notifyItemRemoved(mSelectedItemPosition);
    }
  }

  private void returnToMainActivity() {
    Log.d(TAG, "Returning to Main Activity");
    finish();
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  private void populateEntries() {
    //todo: here is where certain date based functions should run (unless there is a better option)
    recyclerView = findViewById(R.id.recyclerViewHealth);
    if(mShowAll) {
      mHealthActivities = mModerateLivingDAO.getHealthActivitiesByUserAll(mLoggedInUserID);
    } else {
      mHealthActivities = mModerateLivingDAO.getHealthActivitiesByUser(mLoggedInUserID);
    }
    if (mHealthActivities != null) {
      mLivingEntries.clear();
      mLivingEntries.addAll(mHealthActivities);
      mEntryRecyclerAdapter = new EntryRecyclerAdapter(this, mLivingEntries, this);
      recyclerView.setAdapter(mEntryRecyclerAdapter);
    }
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

  @Override
  public void onCheckBoxSelect(int position) {
    ModerateLivingEntries livingEntry = mLivingEntries.get(position);
    HealthActivities healthActivity = mModerateLivingDAO.getHealthActivitiesByID(livingEntry.getID());

    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HealthActivity.this);
    final AlertDialog alertDialog = alertBuilder.create();
    alertBuilder.setMessage("Mark Health Activity complete?\n");
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

            AlertDialog.Builder alertBuilderInner = new AlertDialog.Builder(HealthActivity.this);
            final AlertDialog alertDialogInner = alertBuilderInner.create();

            alertBuilder.setMessage("Would you like to create a new copy of the completed Activity?\n");

            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                mEntryRecyclerAdapter.notifyItemChanged(position);
              }
            });
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //TODO: Call method to create new Health Activity with attributes
                int activityID = mModerateLivingDAO.getMaxHealthActivityID() + 1;
                HealthActivities mHealthActivity = healthActivity.copy(activityID);
                mLivingEntries.add(0, mHealthActivity);
                mModerateLivingDAO.insert(mHealthActivity);
                Util.createEntryLog(getApplicationContext(), mLoggedInUserID, Util.LOG_CREATED, mHealthActivity);
                mEntryRecyclerAdapter.notifyItemInserted(0);
                Toast.makeText(getApplicationContext(), "Creating new Health Activity", Toast.LENGTH_LONG).show();
              }
            });
            AlertDialog checkCreate = alertBuilder.create();
            checkCreate.show();
            new CountDownTimer(1000, 1000) {
              @Override
              public void onTick(long millisUntilFinished) {
              }

              @Override
              public void onFinish() {
                mLivingEntries.remove(position);
                mEntryRecyclerAdapter.notifyItemRemoved(position);
                int completedPoints = healthActivity.getActivityPoints();
                HealthActivityLog healthActivityLog = mModerateLivingDAO.getHealthActivityLogByID(healthActivity.getActivityID());
                Util.logToUserLog(getApplicationContext(), mLoggedInUserID, Util.LOG_COMPLETED, healthActivityLog);
                healthActivity.setIsComplete(true);
                mModerateLivingDAO.update(healthActivity);
                updateUserPoints(completedPoints);
                Toast.makeText(getApplicationContext(),
                    "Activity completed: "+healthActivity.getActivityName()+" with "+completedPoints +" pts",Toast.LENGTH_LONG).show();
              }
            }.start();
          }
        });
    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int i) {
        mEntryRecyclerAdapter.notifyItemChanged(position);
      }
    });
    alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        mEntryRecyclerAdapter.notifyItemChanged(position);
      }
    });
    AlertDialog checkComplete = alertBuilder.create();
    checkComplete.show();
  }

  @Override
  public void onEntryLongClick(int position) {
    mSelectedItemPosition = position;
    ModerateLivingEntries livingEntry = mLivingEntries.get(position);
    HealthActivities healthActivity = mModerateLivingDAO.getHealthActivitiesByID(livingEntry.getID());
    Intent intent = HealthConfigActivity.intentFactory(getApplicationContext(), healthActivity.getActivityID(), mLoggedInUserID);
    Log.d(TAG, "Switching to Health Config Activity for existing Health Activity: " + healthActivity.getActivityName());
    startActivity(intent);
  }

  private void updateUserPoints(int completedPoints) {
    UserID user = mModerateLivingDAO.getUserByID(mLoggedInUserID);
    int userPoints = user.getPoints();
    userPoints =userPoints +completedPoints;
    user.setPoints(userPoints);
    mModerateLivingDAO.update(user);
  }
}