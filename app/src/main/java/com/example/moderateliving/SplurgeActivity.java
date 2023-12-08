package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.Splurges;
import com.example.moderateliving.databinding.ActivitySplurgeBinding;

import java.util.ArrayList;
import java.util.List;

public class SplurgeActivity extends AppCompatActivity implements RecyclerViewInterface{

  private static final String USER_ID = "com.example.moderateliving.SplurgeActivity_USER_ID";
  private static final String TAG = "SplurgeActivity";
  private static final int LOGOUT_USER = -1;
  private static final int NO_USER = 0;
  private ModerateLivingDAO mModerateLivingDAO;
  private RecyclerView recyclerView;
  private HealthRecyclerAdapter mRecyclerAdapter;
  private ActivitySplurgeBinding mSplurgeActivityBinding;
  private Button mButtonSplurgeActivityHome;
  private Button mButtonSplurgeActivityCreate;
  private List<Splurges> mSplurges;
  List<ModerateLivingEntries> mLivingEntries = new ArrayList<>();
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
        createNewSplurge();
      }
    });

    mButtonSplurgeActivityHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToMainActivity(mModerateLivingDAO.getUserByID(mLoggedInUserID).getHashPassword());
      }
    });

  }

  private void populateEntries() {
    recyclerView = findViewById(R.id.recyclerViewSplurge);
    mSplurges = mModerateLivingDAO.getSplurgesByUserID(mLoggedInUserID);
    if(mSplurges != null) {
      mLivingEntries.addAll(mSplurges);
    }
    mRecyclerAdapter = new HealthRecyclerAdapter(this, mLivingEntries, this);
    recyclerView.setAdapter(mRecyclerAdapter);
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

  private void createNewSplurge() {
  }

  private void returnToMainActivity(int userPassHash) {
    Intent intent = MainActivity.intentFactory(getApplicationContext(), userPassHash);
    Log.d(TAG, "Returning to Main Activity");
    startActivity(intent);
  }

  @Override
  public void onCheckBoxSelect(int activityID, boolean recreate) {

  }

  @Override
  public void onEntryLongClick(int position) {

  }
}