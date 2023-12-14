package com.example.moderateliving.AndroidActivity;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.HealthActivityLog;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivityHealthConfigBinding;

public class HealthConfigActivity extends AppCompatActivity {

  private static final String ACTIVITY_ID = "com.example.moderateliving_ACTIVITY_ID";
  private static final String USER_ID = "com.example.moderateliving_USER_ID";
  private static final String TAG = "HealthConfigActivity";
  private Button mButtonHealthConfigComplete;
  private Button mButtonHealthConfigDelete;
  private Button mButtonHealthConfigUpdate;
  private ImageButton mButtonHealthConfigReturn;
  private CheckBox mCheckBoxHealthActivityIsRecurring;
  private EditText mEditTextHealthActivityName;
  private EditText mEditTextHealthActivityDescription;
  private EditText mEditTextHealthActivityPoints;
  private ActivityHealthConfigBinding mActivityHealthConfigBinding;
  private HealthActivities mHealthActivity;
  private UserID mLoggedInUser;
  private ModerateLivingDAO mModerateLivingDAO;
  private boolean createNew = true;
  private Integer mActivityID;
  private Integer mLoggedInUserID;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health_config);

    mActivityHealthConfigBinding = ActivityHealthConfigBinding.inflate(getLayoutInflater());
    setContentView(mActivityHealthConfigBinding.getRoot());
    int darkGreen = getColor(R.color.greenDark);

    mButtonHealthConfigComplete = mActivityHealthConfigBinding.buttonHealthConfigComplete;
    mButtonHealthConfigDelete = mActivityHealthConfigBinding.buttonHealthConfigDelete;
    mButtonHealthConfigUpdate = mActivityHealthConfigBinding.buttonHealthConfigUpdate;
    mCheckBoxHealthActivityIsRecurring = mActivityHealthConfigBinding.checkBoxHealthActivityIsRecurring;
    mEditTextHealthActivityName = mActivityHealthConfigBinding.editTextHealthActivityName;
    mEditTextHealthActivityDescription = mActivityHealthConfigBinding.editTextHealthActivityDescription;
    mEditTextHealthActivityPoints = mActivityHealthConfigBinding.editTextHealthActivityPoints;
    mButtonHealthConfigReturn = mActivityHealthConfigBinding.imageButtonHealthConfigReturn;

    getDatabase();
    if(!confirmNew()) {
      setUpExisting();
    } else {
      setUpCurrent();
    }
    setCurrentHealthActivity();
    setLoggedInUser();

    mButtonHealthConfigUpdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!submitHealthActivity()) {
          Toast.makeText(getApplicationContext(),
              "Please fill out all fields with proper values and resubmit.", Toast.LENGTH_LONG).show();
          clearDisplay();
        } else {
          AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
          final AlertDialog alertDialog = alertBuilder.create();
        }

      }
    });

    mButtonHealthConfigReturn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToHealthActivity();
      }
    });

    mButtonHealthConfigComplete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        completeHealthActivity();
      }
    });

    mButtonHealthConfigDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteHealthActivity();
      }
    });
  }

  private void deleteHealthActivity() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    final AlertDialog alertDialog = alertBuilder.create();
    alertBuilder.setMessage("Are you sure you wish to delete this Health Activity? This action cannot be undone.");
    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "User: " + mLoggedInUserID + " did not delete Health Activity " + mHealthActivity.getActivityName());
      }
    });
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "User: " + mLoggedInUserID + " elected to delete Health Activity " + mHealthActivity.getActivityName());
        //Util.logToUserLog(getApplicationContext(), mLoggedInUserID, Util.LOG_DELETED, mHealthActivity);
        mModerateLivingDAO.delete(mModerateLivingDAO.getHealthActivityLogByID(mHealthActivity.getActivityID()));
        mModerateLivingDAO.delete(mHealthActivity);
        returnToHealthActivity();
      }
    });
    AlertDialog updateRecord = alertBuilder.create();
    updateRecord.show();
  }

  private void completeHealthActivity() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    final AlertDialog alertDialog = alertBuilder.create();
    alertBuilder.setMessage("Are you sure you wish to mark this Health Activity complete?");
    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "User: " + mLoggedInUserID + " did not complete Health Activity " + mHealthActivity.getActivityName());
      }
    });
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        int completedPoints = mHealthActivity.getActivityPoints();
        UserID user = mModerateLivingDAO.getUserByID(mLoggedInUserID);
        int userPoints = user.getPoints();
        userPoints = userPoints + completedPoints;
        HealthActivityLog healthActivityLog = mModerateLivingDAO.getHealthActivityLogByID(mHealthActivity.getActivityID());

        user.setPoints(userPoints);
        mHealthActivity.setIsComplete(true);
        mModerateLivingDAO.update(user);
        Util.logToUserLog(getApplicationContext(), mLoggedInUserID, Util.LOG_COMPLETED, healthActivityLog);
        Log.d(TAG, "User: " + mLoggedInUserID + " completed Health Activity " + mHealthActivity.getActivityName());
        mModerateLivingDAO.update(mHealthActivity);
        returnToHealthActivity();
      }
    });
    AlertDialog updateRecord = alertBuilder.create();
    updateRecord.show();
  }

  private void setUpCurrent() {
    if(mActivityID == 0) {
      Log.d(TAG, "Current Health Activity was requested, but none found.");
    } else {
      mHealthActivity = mModerateLivingDAO.getHealthActivitiesByID(mActivityID);
      mEditTextHealthActivityName.setText(mHealthActivity.getActivityName());
      mEditTextHealthActivityDescription.setText(mHealthActivity.getActivityDescription());
      mEditTextHealthActivityPoints.setText(mHealthActivity.getActivityPoints());
      mCheckBoxHealthActivityIsRecurring.setChecked(mHealthActivity.isRecurring());
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    finish();
  }
  private void clearDisplay() {
    mCheckBoxHealthActivityIsRecurring.setChecked(false);
    mEditTextHealthActivityDescription.setText("");
    mEditTextHealthActivityName.setText("");
    mEditTextHealthActivityPoints.setText("");
  }

  private boolean submitHealthActivity() {
    boolean readyToSubmit = true;
    String activityName = mEditTextHealthActivityName.getText().toString();
    String activityDescription = mEditTextHealthActivityDescription.getText().toString();
    boolean activityIsRecurring = mCheckBoxHealthActivityIsRecurring.isChecked();
    int activityPoints = -1;
    try {
      activityPoints = Integer.parseInt(mEditTextHealthActivityPoints.getText().toString());
    } catch (NumberFormatException e) {
      Toast.makeText(getApplicationContext(), "Could not process points value. Please re-enter and resubmit", Toast.LENGTH_LONG).show();
      Log.d(TAG, "Attempting to submit Health Activity and failed at parsing integer.");
      readyToSubmit = false;
    }
    if(activityName.isEmpty() || activityDescription.isEmpty() || activityPoints == -1) {
      readyToSubmit = false;
    }

    if(readyToSubmit) {
      if(mHealthActivity == null) {
        int activityID = mModerateLivingDAO.getMaxHealthActivityID() + 1;
        mHealthActivity = new HealthActivities(
            activityID,
            mLoggedInUserID,
            activityName,
            activityDescription,
            activityPoints,
            activityIsRecurring
        );
        mModerateLivingDAO.insert(mHealthActivity);
        Util.createEntryLog(getApplicationContext(), mLoggedInUserID, Util.LOG_CREATED, mHealthActivity);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Would you like to create another Health Activity");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            clearDisplay();
            mActivityID = 0;
            mHealthActivity = null;
          }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            returnToHealthActivity();
          }
        });

        AlertDialog checkResubmit = alertBuilder.create();
        checkResubmit.show();
      } else {
        HealthActivityLog healthActivityLog = mModerateLivingDAO.getHealthActivityLogByID(mHealthActivity.getActivityID());
        mHealthActivity.setActivityName(activityName);
        mHealthActivity.setActivityDescription(activityDescription);
        mHealthActivity.setActivityPoints(activityPoints);
        mHealthActivity.setRecurring(activityIsRecurring);
        mModerateLivingDAO.update(mHealthActivity);

        Util.logToUserLog(getApplicationContext(), mLoggedInUserID, Util.LOG_UPDATED, healthActivityLog);
        returnToHealthActivity();
      }
    } else {
      Toast.makeText(getApplicationContext(), "Please fill out all fields with proper values and resubmit.", Toast.LENGTH_LONG);
    }
    return readyToSubmit;
  }

  private void returnToHealthActivity() {
    Intent intent = HealthActivity.intentFactory(getApplicationContext(), mLoggedInUserID);
    Log.d(TAG, "Switching to Health Activity");
    startActivity(intent);
  }

  private void setLoggedInUser() {
    mLoggedInUserID = getIntent().getIntExtra(USER_ID, 0);
    if(mLoggedInUserID <= 0) {
      Intent intent = MainActivity.intentFactory(getApplicationContext(), 0);
      Toast.makeText(getApplicationContext(), "No user is currently logged in. Returning to login screen.", Toast.LENGTH_LONG).show();
      Util.logOutUser(this);
      startActivity(intent);
    } else {
      Log.d(TAG, "User not found in Extra.");
    }
  }

  private void setUpExisting() {
    if(mActivityID != 0) {
      mHealthActivity = mModerateLivingDAO.getHealthActivitiesByID(mActivityID);
      String buttonUpdate = "Update Activity";
      String activityPoints = mHealthActivity.getActivityPoints() +"";
      mHealthActivity = mModerateLivingDAO.getHealthActivitiesByID(mActivityID);
      mButtonHealthConfigUpdate.setText(buttonUpdate);
      mButtonHealthConfigComplete.setVisibility(Button.VISIBLE);
      mButtonHealthConfigDelete.setVisibility(Button.VISIBLE);
      mEditTextHealthActivityName.setText(mHealthActivity.getActivityName());
      mEditTextHealthActivityDescription.setText(mHealthActivity.getActivityDescription());
      mEditTextHealthActivityPoints.setText(activityPoints);
      mCheckBoxHealthActivityIsRecurring.setChecked(mHealthActivity.isRecurring());
    }
  }

  private boolean confirmNew() {
    mActivityID = getIntent().getIntExtra(ACTIVITY_ID, 0);
    if(mActivityID != 0) {
      return false;
    }
    return true;
  }

  private void setCurrentHealthActivity() {
    int activityID = getIntent().getIntExtra(ACTIVITY_ID, 0);
    int userID = getIntent().getIntExtra(USER_ID, 0);
    if(activityID != 0) {
      mHealthActivity = mModerateLivingDAO.getHealthActivitiesByID(activityID);
    } else {
      Log.d(TAG, "Could not find Health Activity in Extra.");
    }
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  public static Intent intentFactory(Context packageContext, int activityID, int userID) {
    Intent intent = new Intent(packageContext, HealthConfigActivity.class);
    intent.putExtra(ACTIVITY_ID, activityID);
    intent.putExtra(USER_ID, userID);
    return intent;
  }
}