package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.EntryRecyclerAdapter;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.UserRecyclerViewAdapter;
import com.example.moderateliving.UserRecyclerViewInterface;
import com.example.moderateliving.databinding.ActivityUserManagementBinding;

import java.util.HashMap;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements UserRecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.UserManagement_USER_ID";
  private static final String TAG = "UserManagementActivity";
  private static final int LOGOUT_USER = -1;
  private static final int NO_USER = 0;

  List<UserID> mUsers;
  private ActivityUserManagementBinding mActivityUserManagementBinding;
  private ModerateLivingDAO mModerateLivingDAO;
  private RecyclerView recyclerView;
  private UserRecyclerViewAdapter mUserRecyclerAdapter;
  private Button mButtonUserManagementReturnHome;
  private ImageButton mButtonUserManagementAdd;
  private ImageButton mButtonUserManagementDelete;
  private ImageButton mButtonUserManagementEdit;
  private ImageButton mButtonUserManagementResetPassword;
  private int mLoggedInUserID;
  private int mSelectedPosition;

  //TODO: Modify to be used with Fragments and Recycler view - this may not be done
  //TODO: Functionality: include ability to add new user with admin rights
  //TODO: Functionality: include ability to grant admin rights to user
  //TODO: Functionality: include ability to delete user
  //TODO: Functionality: include ability to reset user password
  //TODO: Functionality: include ability to edit user settings
  //TODO: onClick for Return Home

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_management);

    mActivityUserManagementBinding = ActivityUserManagementBinding.inflate(getLayoutInflater());
    setContentView(mActivityUserManagementBinding.getRoot());

    initializeBindings();

    getDatabase();
    checkUserLoggedIn();
    populateEntries();

    mButtonUserManagementReturnHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity();
      }
    });

    mButtonUserManagementAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

    mButtonUserManagementDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

    mButtonUserManagementEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mUsers.size() > mSelectedPosition) {
          Intent intent = SignUpActivity.intentFactory(getApplicationContext(), mUsers.get(mSelectedPosition).getUserID());
          startActivity(intent);
          mUserRecyclerAdapter.notifyItemChanged(mSelectedPosition);
        }
      }
    });

    mButtonUserManagementResetPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    if(mUsers.size() > mSelectedPosition) {
      UserID mUser = mUsers.get(mSelectedPosition);
      mUsers.remove(mSelectedPosition);
      mUser = mModerateLivingDAO.getUserByID(mUser.getUserID());
      mUsers.add(mSelectedPosition, mUser);
      mUserRecyclerAdapter.notifyItemChanged(mSelectedPosition);
    }
  }

  private void initializeBindings() {
    mButtonUserManagementReturnHome = mActivityUserManagementBinding.buttonUserManagementReturnHome;
    mButtonUserManagementAdd = mActivityUserManagementBinding.imageButtonUserManagementAdd;
    mButtonUserManagementDelete = mActivityUserManagementBinding.imageButtonUserManagementDelete;
    mButtonUserManagementEdit = mActivityUserManagementBinding.imageButtonUserManagementEdit;
    mButtonUserManagementResetPassword = mActivityUserManagementBinding.imageButtonUserManagementResetPassword;
  }

  public static Intent intentFactory(Context packageContext, int mUserID) {
    Intent intent = new Intent(packageContext, UserManagementActivity.class);
    intent.putExtra(USER_ID, mUserID);
    return intent;
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

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  private void populateEntries() {
    recyclerView = findViewById(R.id.recyclerViewUserManagement);
    mUsers = mModerateLivingDAO.getUserIDs();
    if (mUsers != null) {
      mUserRecyclerAdapter = new UserRecyclerViewAdapter(this, mUsers, this);
      recyclerView.setAdapter(mUserRecyclerAdapter);
    }
  }

  @Override
  public void clickToSelect(int selectedPosition) {
    mSelectedPosition = selectedPosition;
  }
}
