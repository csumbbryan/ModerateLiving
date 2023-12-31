package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.EntryRecyclerAdapter;
import com.example.moderateliving.ModerateLivingEntries;
import com.example.moderateliving.R;
import com.example.moderateliving.RecyclerViewInterface;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.Splurges;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivitySplurgeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Support creation, review, and completion of "Splurge" type tasks.
 */
//TODO: Consider LiveData?
public class SplurgeActivity extends AppCompatActivity implements RecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.SplurgeActivity_USER_ID";
  private static final String TAG = "SplurgeActivity";
  private static final int LOGOUT_USER = -1;
  private static final int NO_USER = 0;
  private static final String LOG_CREATED = "created";
  private static final String LOG_REDEEMED = "redeemed";
  private ModerateLivingDAO mModerateLivingDAO;
  private RecyclerView recyclerView;
  private EntryRecyclerAdapter mEntryRecyclerAdapter;
  private ActivitySplurgeBinding mSplurgeActivityBinding;
  private Button mButtonSplurgeActivityHome;
  private Button mButtonSplurgeActivityCreate;
  private List<Splurges> mSplurges;
  private List<ModerateLivingEntries> mLivingEntries = new ArrayList<>();
  private int mLoggedInUserID = NO_USER;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splurge);

    mSplurgeActivityBinding = ActivitySplurgeBinding.inflate(getLayoutInflater());
    setContentView(mSplurgeActivityBinding.getRoot());

    mButtonSplurgeActivityHome = mSplurgeActivityBinding.buttonSplurgeActivityHome;
    mButtonSplurgeActivityCreate = mSplurgeActivityBinding.buttonSplurgeActivityCreate;

    getDatabase();
    checkUserLoggedIn();
    populateEntries();

    mButtonSplurgeActivityCreate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toCreateNewSplurge();
      }
    });

    mButtonSplurgeActivityHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity();
      }
    });

  }

  @Override
  protected void onRestart() {
    super.onRestart();
    int splurgeActivityCount = mModerateLivingDAO.getSplurgesByUserID(mLoggedInUserID).size();
    if(splurgeActivityCount > 0 && splurgeActivityCount > mLivingEntries.size()) {
      int index = mLivingEntries.size();
      List<Splurges> newSplurges = mModerateLivingDAO.getSplurgesByUserID(mLoggedInUserID);
      for(int i = index; i < splurgeActivityCount; i++) {
        mLivingEntries.add(i, newSplurges.get(i));
        mEntryRecyclerAdapter.notifyItemInserted(i);
      }
    }
  }

  private void populateEntries() {
    recyclerView = findViewById(R.id.recyclerViewSplurge);
    mSplurges = mModerateLivingDAO.getSplurgesByUserID(mLoggedInUserID);
    if(mSplurges != null) {
      mLivingEntries.addAll(mSplurges);
      mEntryRecyclerAdapter = new EntryRecyclerAdapter(this, mLivingEntries, this);
      recyclerView.setAdapter(mEntryRecyclerAdapter);
    }
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
          returnToMainActivity();
        }
      }.start();

    }
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  public static Intent intentFactory(Context packageContext, int mLoggedInUserID) {
    Intent intent = new Intent(packageContext, SplurgeActivity.class);
    intent.putExtra(USER_ID, mLoggedInUserID);
    return intent;
  }

  private void toCreateNewSplurge() {
    int mSplurgeID = 0;
    Intent intent = SplurgeConfigActivity.intentFactory(getApplicationContext(), mSplurgeID, mLoggedInUserID);
    Log.d(TAG, "Switching to Splurge Config Activity View for new Splurge");
    startActivity(intent);
  }

  private void returnToMainActivity() {
    Log.d(TAG, "Returning to Main Activity");
    finish();
  }

  @Override
  public void onCheckBoxSelect(int position) {
    ModerateLivingEntries livingEntry = mLivingEntries.get(position);
    //Splurges splurge = mModerateLivingDAO.getSplurgeByID(itemPosition);
    Splurges splurge = mModerateLivingDAO.getSplurgeByID(livingEntry.getID());


    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SplurgeActivity.this);
    final AlertDialog alertDialog = alertBuilder.create();

    alertBuilder.setMessage("Redeem Splurge?\n");
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        mEntryRecyclerAdapter.notifyItemChanged(position);
        int redeemedPoints = splurge.getPointsCost();
        UserID user = mModerateLivingDAO.getUserByID(mLoggedInUserID);
        int userPoints = user.getPoints();
        userPoints = userPoints - redeemedPoints;
        if(userPoints < 0) {
          Toast.makeText(getApplicationContext(), "You do not have enough points to redeem that Splurge.", Toast.LENGTH_LONG);
          Toast.makeText(getApplicationContext(),
              "Splurge redeem attempted but not enough points: " + splurge.getSplurgeName() + " with " + redeemedPoints + " pts", Toast.LENGTH_LONG).show();
        } else {
          user.setPoints(userPoints);
          mModerateLivingDAO.update(user);
          Util.createEntryLog(getApplicationContext(), mLoggedInUserID, Util.LOG_REDEEMED, splurge);
          Toast.makeText(getApplicationContext(),
              "Splurge redeemed: " + splurge.getSplurgeName() + " with " + redeemedPoints + " pts", Toast.LENGTH_LONG).show();
        }
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
    AlertDialog checkDelete = alertBuilder.create();
    checkDelete.show();




  }

  @Override
  public void onEntryLongClick(int position) {
    ModerateLivingEntries livingEntry = mLivingEntries.get(position);
    Splurges splurge = mModerateLivingDAO.getSplurgeByID(livingEntry.getID());
    Intent intent = SplurgeConfigActivity.intentFactory(getApplicationContext(), splurge.getSplurgeID(), mLoggedInUserID);
    Log.d(TAG, "Switching to Splurge Config Activity for existing Splurge: " + splurge.getSplurgeName());
    startActivity(intent);
  }
}