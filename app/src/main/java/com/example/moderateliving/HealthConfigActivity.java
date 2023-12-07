package com.example.moderateliving;

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
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.UserID;
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
  private HealthActivities mHealthActivities;
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

    mButtonHealthConfigComplete = mActivityHealthConfigBinding.buttonHealthConfigComplete;
    mButtonHealthConfigDelete = mActivityHealthConfigBinding.buttonHealthConfigDelete;
    mButtonHealthConfigUpdate = mActivityHealthConfigBinding.buttonHealthConfigUpdate;
    mCheckBoxHealthActivityIsRecurring = mActivityHealthConfigBinding.checkBoxHealthActivityIsRecurring;
    mEditTextHealthActivityName = mActivityHealthConfigBinding.editTextHealthActivityName;
    mEditTextHealthActivityDescription = mActivityHealthConfigBinding.editTextHealthActivityDescription;
    mEditTextHealthActivityPoints = mActivityHealthConfigBinding.editTextHealthActivityPoints;
    mButtonHealthConfigReturn = mActivityHealthConfigBinding.imageButtonHealthConfigReturn;

    Util.logOutUser(this);

    getDatabase();
    if(!confirmNew()) {
      setUpExisting();
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
        Intent intent = HealthActivity.intentFactory(getApplicationContext(), mLoggedInUserID);
        Log.d(TAG, "Switching to Health Activity");
        startActivity(intent);
      }
    });

    //TODO: Complete listener for Complete Button
    //TODO: Complete listener for Delete Button
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

  //TODO: Log to HealthActivityLog
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

    //TODO: Consider checking for duplicates? If so, possibly use Dialog check to confirm duplicate creation
    if(mHealthActivities == null && readyToSubmit) {
      mHealthActivities = new HealthActivities(
          mLoggedInUserID,
          activityName,
          activityDescription,
          activityPoints,
          activityIsRecurring
      );

      if(readyToSubmit) {
        mModerateLivingDAO.insert(mHealthActivities);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Would you like to create another Health Activity");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            clearDisplay();
            mActivityID = 0;
            mHealthActivities = null;
          }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Intent intent = HealthActivity.intentFactory(getApplicationContext(), mLoggedInUserID);
            startActivity(intent);
          }
        });

        AlertDialog checkResubmit = alertBuilder.create();
        checkResubmit.show();
      } else {
        Toast.makeText(getApplicationContext(), "Please fill out all fields with proper values and resubmit.", Toast.LENGTH_LONG);
      }
    } else {
      if(readyToSubmit) {
        mModerateLivingDAO.update(mHealthActivities);
      }
    }
    return readyToSubmit;
  }

  private void setLoggedInUser() {
    mLoggedInUserID = getIntent().getIntExtra(USER_ID, 0);
    if(mLoggedInUserID <= 0) {
      Intent intent = MainActivity.intentFactory(getApplicationContext(), 0);
      Toast.makeText(getApplicationContext(), "No user is currently logged in. Returning to login screen.", Toast.LENGTH_LONG).show();
      Util.logOutUser(this);
      startActivity(intent);
    } else {
      //TODO: Modify text to display username?
    }

  }

  private void setUpExisting() {
    if(mActivityID != 0) {
      mHealthActivities = mModerateLivingDAO.getHealthActivitiesByID(mActivityID);
      String buttonUpdate = "Update Activity";
      String activityPoints = mHealthActivities.getActivityPoints() +"";
      mHealthActivities = mModerateLivingDAO.getHealthActivitiesByID(mActivityID);
      mButtonHealthConfigUpdate.setText(buttonUpdate);
      mButtonHealthConfigComplete.setVisibility(Button.VISIBLE);
      mButtonHealthConfigDelete.setVisibility(Button.VISIBLE);
      mEditTextHealthActivityName.setText(mHealthActivities.getActivityName());
      mEditTextHealthActivityDescription.setText(mHealthActivities.getActivityDescription());
      mEditTextHealthActivityPoints.setText(activityPoints);
      mCheckBoxHealthActivityIsRecurring.setChecked(mHealthActivities.isRecurring());
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
      mHealthActivities = mModerateLivingDAO.getHealthActivitiesByID(activityID);

    } else {
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