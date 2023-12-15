package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: administrative view for managing users.
 * Enables admins to create, edit, and delete user acounts. Includes support for enabling
 * admin rights through user edit.
 */
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

  //TODO: feature enhancement: Modify to be used with Fragments and Recycler view
  //TODO: feature enhancement: include ability to reset user password

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
        Intent intent = SignUpActivity.intentFactory(getApplicationContext(), NO_USER);
        Log.d(TAG, "Switching to SignUp User Activity to add new user.");
        startActivity(intent);
      }
    });

    mButtonUserManagementDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteUser();
      }
    });

    mButtonUserManagementEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mUsers.size() > mSelectedPosition) {
          Intent intent = SignUpActivity.intentFactory(getApplicationContext(), mUsers.get(mSelectedPosition).getUserID());
          Log.d(TAG, "Switching to SignUp Activity View to edit user: " + mUsers.get(mSelectedPosition).getUserID());
          startActivity(intent);
        }
      }
    });

    mButtonUserManagementResetPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
  }

  private void deleteUser() {
    if(mUsers.size() > mSelectedPosition) {
      UserID user = mUsers.get(mSelectedPosition);
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserManagementActivity.this);
      final AlertDialog alertDialog = alertBuilder.create();
      alertBuilder.setMessage("Are you sure you wish to delete user " + user.getUsername() + " ?");
      alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
      });
      alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          mUsers.remove(user);
          mModerateLivingDAO.delete(user);
          mUserRecyclerAdapter.notifyItemRemoved(mSelectedPosition);
          Log.d(TAG, "Deleting user " + user.getUsername() + " from UserID database.");
        }
      });
      AlertDialog deleteUser = alertBuilder.create();
      deleteUser.show();
    }
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    int userIDCount = mModerateLivingDAO.getUserIDs().size();
    //getCreatedItem
    if(mUsers.size() < userIDCount) {
      mUsers.add(mModerateLivingDAO.getUserByID(mModerateLivingDAO.getMaxUserID()));
      mUserRecyclerAdapter.notifyItemInserted(userIDCount-1);
    }
    //getUpdatedItem
    if(mUsers.size() == userIDCount && mUsers.size() > mSelectedPosition) {
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
