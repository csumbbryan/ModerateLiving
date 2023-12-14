package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.TableClasses.SplurgeLog;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivitySplurgeConfigBinding;
import com.example.moderateliving.TableClasses.Splurges;

public class SplurgeConfigActivity extends AppCompatActivity {

  public static final String USER_ID = "com.example.moderateliving.SplurgeConfigActivity_USER_ID";
  private static final String TAG = "SplurgeConfigActivity";
  private static final String SPLURGE_ID = "com.example.moderateliving.SplurgeConfigActivity_SPLURGE_ID";
  private ActivitySplurgeConfigBinding mSplurgeConfigActivityBinding;
  private ModerateLivingDAO mModerateLivingDAO;
  private Button mButtonSplurgeConfigComplete;
  private Button mButtonSplurgeConfigDelete;
  private Button mButtonSplurgeConfigUpdate;
  private ImageButton mButtonSplurgeConfigReturn;
  private EditText mEditTextSplurgeActivityName;
  private EditText mEditTextSplurgeActivityDescription;
  private EditText mEditTextSplurgePoints;
  private int mSplurgeID;
  private Splurges mSplurge;
  private int mLoggedInUserID;
  private int mMaxSplurgeID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splurge_config);

    mSplurgeConfigActivityBinding = ActivitySplurgeConfigBinding.inflate(getLayoutInflater());
    setContentView(mSplurgeConfigActivityBinding.getRoot());

    mButtonSplurgeConfigComplete = mSplurgeConfigActivityBinding.buttonSplurgeConfigComplete;
    mButtonSplurgeConfigDelete = mSplurgeConfigActivityBinding.buttonSplurgeConfigDelete;
    mButtonSplurgeConfigUpdate = mSplurgeConfigActivityBinding.buttonSplurgeConfigUpdate;
    mButtonSplurgeConfigReturn = mSplurgeConfigActivityBinding.imageButtonSplurgeConfigReturn;
    mEditTextSplurgeActivityName = mSplurgeConfigActivityBinding.editTextSplurgeActivityName;
    mEditTextSplurgeActivityDescription = mSplurgeConfigActivityBinding.editTextSplurgeActivityDescription;
    mEditTextSplurgePoints = mSplurgeConfigActivityBinding.editTextSplurgePoints;

    getDatabase();
    if(!confirmNew()) {
      setUpExisting();
    } else {
      setUpCurrent();
    }
    setCurrentSplurge();
    setLoggedInUser();

    mButtonSplurgeConfigUpdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!submitSplurge()) {
          Toast.makeText(getApplicationContext(),
              "Please fill out all fields with proper values and resubmit.", Toast.LENGTH_LONG).show(); //TODO: Is this needed?
          clearDisplay();
        } else {
          AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
          final AlertDialog alertDialog = alertBuilder.create();
        }

      }
    });

    mButtonSplurgeConfigReturn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToSplurgeActivity();
      }
    });

    mButtonSplurgeConfigComplete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        completeSplurge();
      }
    });

    mButtonSplurgeConfigDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteSplurgeActivity();
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    finish();
  }

  private void deleteSplurgeActivity() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    final AlertDialog alertDialog = alertBuilder.create();
    alertBuilder.setMessage("Are you sure you wish to delete this Splurge?");
    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "User: " + mLoggedInUserID + " did not delete Splurge " + mSplurge.getSplurgeName());
      }
    });
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "User: " + mLoggedInUserID + " elected to delete Splurge " + mSplurge.getSplurgeName());
        mModerateLivingDAO.delete(mSplurge);
        returnToSplurgeActivity();
        //TODO: setup to log(?) but do not record as complete
      }
    });
    AlertDialog deleteRecord = alertBuilder.create();
    deleteRecord.show();
  }

  private void completeSplurge() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    final AlertDialog alertDialog = alertBuilder.create();
    alertBuilder.setMessage("Are you sure you wish to redeem this Splurge?");
    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "User: " + mLoggedInUserID + " did not redeem Splurge " + mSplurge.getSplurgeName());
      }
    });
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        int redeemedPoints = mSplurge.getPointsCost();
        UserID user = mModerateLivingDAO.getUserByID(mLoggedInUserID);
        int userPoints = user.getPoints();
        userPoints = userPoints + redeemedPoints;
        SplurgeLog splurgeLog = mModerateLivingDAO.getSplurgeLogByID(mSplurge.getSplurgeID()); //TODO: Review to make sure this works

        user.setPoints(userPoints);
        mModerateLivingDAO.update(user);
        Log.d(TAG, "User: " + mLoggedInUserID + " redeemed Splurge " + mSplurge.getSplurgeName());
        Util.logToUserLog(getApplicationContext(), mLoggedInUserID, Util.LOG_REDEEMED, splurgeLog);
        //mModerateLivingDAO.delete(mSplurge); //TODO: Review behavior for Splurge redemption
        returnToSplurgeActivity();
        //TODO: log in SplurgeLog as complete
      }
    });
    AlertDialog completeRecord = alertBuilder.create();
    completeRecord.show();
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

  private void setCurrentSplurge() {
    int splurgeID = getIntent().getIntExtra(SPLURGE_ID, 0);
    int userID = getIntent().getIntExtra(USER_ID, 0);
    mMaxSplurgeID = 0;
    if(splurgeID != 0) {
      mSplurge = mModerateLivingDAO.getSplurgeByID(splurgeID);
    } else {
      //TODO: setup warning, log, etc.?
    }
  }

  private void setUpCurrent() {
    if(mSplurgeID == 0) {
      //TODO: Warning, logs, etc.
    } else {
      mSplurge = mModerateLivingDAO.getSplurgeByID(mSplurgeID);
      mEditTextSplurgeActivityName.setText(mSplurge.getSplurgeName());
      mEditTextSplurgeActivityDescription.setText(mSplurge.getSplurgeDescription());
      mEditTextSplurgePoints.setText(mSplurge.getPointsCost());
    }
  }

  private void setUpExisting() {
    if(mSplurgeID != 0) {
      mSplurge = mModerateLivingDAO.getSplurgeByID(mSplurgeID);
      String buttonUpdate = "Update Splurge";
      String activityPoints = mSplurge.getPointsCost() +"";
      mSplurge = mModerateLivingDAO.getSplurgeByID(mSplurgeID);
      mButtonSplurgeConfigUpdate.setText(buttonUpdate);
      mButtonSplurgeConfigComplete.setVisibility(Button.VISIBLE);
      mButtonSplurgeConfigDelete.setVisibility(Button.VISIBLE);
      mEditTextSplurgeActivityName.setText(mSplurge.getSplurgeName());
      mEditTextSplurgeActivityDescription.setText(mSplurge.getSplurgeDescription());
      mEditTextSplurgePoints.setText(activityPoints);
    }
  }

  private boolean confirmNew() {
    mSplurgeID = getIntent().getIntExtra(SPLURGE_ID, 0);
    if(mSplurgeID != 0) {
      return false;
    }
    return true;
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  public static Intent intentFactory(Context packageContext, int mSplurgeID, int mLoggedInUserID) {
    Intent intent = new Intent(packageContext, SplurgeConfigActivity.class);
    intent.putExtra(USER_ID, mLoggedInUserID);
    intent.putExtra(SPLURGE_ID, mSplurgeID);
    return intent;
  }

  public boolean submitSplurge() {
    boolean readyToSubmit = true;
    String splurgeName = mEditTextSplurgeActivityName.getText().toString();
    String splurgeDescription = mEditTextSplurgeActivityDescription.getText().toString();
    int splurgeCost = -1;
    try {
      splurgeCost = Integer.parseInt(mEditTextSplurgePoints.getText().toString());
    } catch (NumberFormatException e) {
      Toast.makeText(getApplicationContext(), "Could not process points value. Please re-enter and resubmit", Toast.LENGTH_LONG).show();
      Log.d(TAG, "Attempting to submit Splurge and failed at parsing integer.");
      readyToSubmit = false;
    }
    if(splurgeName.isEmpty() || splurgeDescription.isEmpty() || splurgeCost == -1) {
      readyToSubmit = false;
    }
    if(!mModerateLivingDAO.getSplurgesByNameAndUserID(splurgeName, mLoggedInUserID).isEmpty()) {
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
      final AlertDialog alertDialog = alertBuilder.create();

      alertBuilder.setMessage("That Splurge already exists. Would you like to create a duplicate Splurge?");
      alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          //TODO: Clean up these methods, call separate methods?
          mMaxSplurgeID = mModerateLivingDAO.getMaxSplurgeID() + 1;
          Splurges mSplurgeCopy = mSplurge.copy(mMaxSplurgeID);
          mModerateLivingDAO.insert(mSplurgeCopy);
          Util.createEntryLog(getApplicationContext(), mLoggedInUserID, Util.LOG_CREATED, mSplurgeCopy);
        }
      });
      alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          returnToSplurgeActivity();
        }
      });
      AlertDialog checkResubmit = alertBuilder.create();
      checkResubmit.show();
    }
    if(readyToSubmit) {
      if(mSplurge == null) {
        int splurgeID = mModerateLivingDAO.getMaxSplurgeID() + 1;
        mSplurge = new Splurges(
            splurgeID,
            mLoggedInUserID,
            splurgeName,
            splurgeDescription,
            splurgeCost
        );
        mModerateLivingDAO.insert(mSplurge);
        Util.createEntryLog(getApplicationContext(), mLoggedInUserID, Util.LOG_CREATED, mSplurge);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Would you like to create another Splurge?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            clearDisplay();
            mSplurgeID = 0;
            mSplurge = null;
          }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            returnToSplurgeActivity();
          }
        });

        AlertDialog checkResubmit = alertBuilder.create();
        checkResubmit.show();
      } else {
        mSplurge.setSplurgeName(splurgeName);
        mSplurge.setSplurgeDescription(splurgeDescription);
        mSplurge.setPointsCost(splurgeCost);
        mModerateLivingDAO.update(mSplurge);
        Util.createEntryLog(getApplicationContext(), mLoggedInUserID, Util.LOG_UPDATED, mSplurge);
        returnToSplurgeActivity();
      }
    } else {
      Toast.makeText(getApplicationContext(), "Please fill out all fields with proper values and resubmit.", Toast.LENGTH_LONG).show();
    }
    return readyToSubmit;
  }

  private void clearDisplay() {
    mEditTextSplurgePoints.setText("");
    mEditTextSplurgeActivityName.setText("");
    mEditTextSplurgeActivityDescription.setText("");
  }

  private void returnToSplurgeActivity() {
    Intent intent = SplurgeActivity.intentFactory(getApplicationContext(), mLoggedInUserID);
    Log.d(TAG, "Returning to SplurgeActivity View");
    startActivity(intent);
  }
}