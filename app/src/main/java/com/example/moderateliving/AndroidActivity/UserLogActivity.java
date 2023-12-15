package com.example.moderateliving.AndroidActivity;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.LogRecyclerAdapter;
import com.example.moderateliving.LogRecyclerViewInterface;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.TableClasses.HealthActivityLog;
import com.example.moderateliving.TableClasses.SplurgeLog;
import com.example.moderateliving.TableClasses.UserLog;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivityUserLogBinding;

import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: View of User logs. Includes Completion, Creation, and Redemption tasks status.
 * Allows undo of certain tasks based on task type and object state of Health Activity or Splurge
 */
public class UserLogActivity extends AppCompatActivity implements LogRecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.UserLogActivity_USER_ID";
  private static final String SHOW_ALL_USERS_LOGS = "com.example.moderateliving.UserLogActivity_SHOW_ALL_USERS_LOGS";
  public static final int DEFAULT_COLOR = 0xFFFFFFFF;
  public static final int SELECTED_COLOR = 0xAAEEEEEE;
  private static final int NO_USER = 0;
  private static final int LOGOUT_USER = -1;
  private static final String TAG = "User Log Activity";

  private ActivityUserLogBinding mUserLogBinding;
  private ModerateLivingDAO mModerateLivingDAO;
  private RecyclerView recyclerView;
  private LogRecyclerAdapter mLogRecyclerAdapter;
  private Button mButtonUserLogReturnHome;
  private ImageButton mImageButtonUserLogDelete;
  private ImageButton mImageButtonUserLogUndo;
  private TextView mTextViewUserLogUsername;
  private int mLoggedInUserID;
  private int mSelectedItemPosition;
  private boolean isAdminLoggedIn = false;
  private boolean mShowAllUsersLogs = false;

  List<UserLog> userLogs;

  public static Intent intentFactory(Context packageContext, int mLoggedInUserID, boolean showAllUsersLogs) {
    Intent intent = new Intent(packageContext, UserLogActivity.class);
    intent.putExtra(USER_ID, mLoggedInUserID);
    intent.putExtra(SHOW_ALL_USERS_LOGS, showAllUsersLogs);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_log);

    mUserLogBinding = ActivityUserLogBinding.inflate(getLayoutInflater());
    setContentView(mUserLogBinding.getRoot());

    getDatabase();
    getExtras();
    checkUserLoggedIn();
    populateEntries();

    mButtonUserLogReturnHome = mUserLogBinding.buttonUserLogReturnHome;
    mImageButtonUserLogDelete = mUserLogBinding.imageButtonUserLogDelete;
    mImageButtonUserLogUndo = mUserLogBinding.imageButtonUserLogUndo;
    mTextViewUserLogUsername = mUserLogBinding.textViewUserLogUserNameDisplay;

    mTextViewUserLogUsername.setText(mModerateLivingDAO.getUserByID(mLoggedInUserID).getName());

    mButtonUserLogReturnHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity();
      }
    });

    mImageButtonUserLogDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(userLogs.size() > mSelectedItemPosition) {
          mModerateLivingDAO.delete(userLogs.get(mSelectedItemPosition));
          userLogs.remove(mSelectedItemPosition);
          mLogRecyclerAdapter.notifyItemRemoved(mSelectedItemPosition);
        }
      }
    });

    mImageButtonUserLogUndo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //TODO: enhancement request: better check for null item IDs
        if(userLogs.size() > mSelectedItemPosition) {
          UserLog userLog = userLogs.get(mSelectedItemPosition);
          boolean userLogUndid = false;
          if (userLog.getActivityType().equals(Util.TYPE_HEALTH_ACTIVITY)) {
            int userItemID = userLog.getItemID();
            HealthActivityLog healthActivityLog = mModerateLivingDAO.getHealthActivityLogByLogID(userItemID);
            if (userLog.getDescription().equals(Util.LOG_CREATED)) {
              userLogUndid = Util.undoCreateHealthActivity(getApplicationContext(), mLoggedInUserID, healthActivityLog);
            } else if (userLog.getDescription().equals(Util.LOG_COMPLETED)) {
              userLogUndid = Util.undoCompleteHealthActivity(getApplicationContext(), mLoggedInUserID, healthActivityLog);
            } else {
              Toast.makeText(getApplicationContext(), "User Log entry cannot be undone", Toast.LENGTH_LONG).show();
            }
          } else if (userLog.getActivityType().equals(Util.TYPE_SPLURGE)) {
            int userItemID = userLog.getItemID();
            SplurgeLog splurgeLog = mModerateLivingDAO.getSplurgeLogByLogID(userItemID);
            if (userLog.getDescription().equals(Util.LOG_CREATED)) {
              userLogUndid = Util.undoCreateSplurge(getApplicationContext(), mLoggedInUserID, splurgeLog);
            } else if (userLog.getDescription().equals(Util.LOG_REDEEMED)) {
              userLogUndid = Util.undoRedeem(getApplicationContext(), mLoggedInUserID, splurgeLog);
            } else {
              Toast.makeText(getApplicationContext(), "User Log entry cannot be undone", Toast.LENGTH_LONG).show();
            }
          } else {
            Toast.makeText(getApplicationContext(), "User Activity type not found.", Toast.LENGTH_LONG).show();
          }
          if(userLogUndid) {
            Toast.makeText(getApplicationContext(), "Successfully rolled back action on " + userLog.getLogEntryName(), Toast.LENGTH_LONG).show();
            mModerateLivingDAO.delete(userLog);
            userLogs.remove(mSelectedItemPosition);
            mLogRecyclerAdapter.notifyItemRemoved(mSelectedItemPosition);
          }
        }
      }
    });

  }

  private void getExtras() {
    mLoggedInUserID = getIntent().getIntExtra(USER_ID, NO_USER);
    mShowAllUsersLogs = getIntent().getBooleanExtra(SHOW_ALL_USERS_LOGS, false);
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  private void checkUserLoggedIn() {

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
    } else {
      isAdminLoggedIn = mModerateLivingDAO.getUserByID(mLoggedInUserID).getIsAdmin();
    }
  }

  private void returnToMainActivity() {
    Log.d(TAG, "Returning to Main Activity");
    finish();
  }

  private void populateEntries() {
    if(isAdminLoggedIn && mShowAllUsersLogs) {
      userLogs = mModerateLivingDAO.getUserLogs();
    } else {
      userLogs = mModerateLivingDAO.getUserLogByUserID(mLoggedInUserID);
    }
    recyclerView = findViewById(R.id.recyclerViewUserLog);
    if (userLogs != null) {
      mLogRecyclerAdapter = new LogRecyclerAdapter(this, userLogs, this);
      recyclerView.setAdapter(mLogRecyclerAdapter);
    }
  }

  @Override
  public void clickedSelected(int selectedPosition) {
     mSelectedItemPosition = selectedPosition;
  }

  @Override
  public void longClickToSelect(int position) {

  }
}