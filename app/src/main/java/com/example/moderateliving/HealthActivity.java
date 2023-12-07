package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityHealthBinding;

import java.util.HashMap;
import java.util.List;

//TODO: add delete and logout button
//TODO: add functionality to add points for user
//TODO: add functionality to limit health-activity creation for unique names
//TODO: move activities to activity log??
public class HealthActivity extends AppCompatActivity implements RecyclerViewInterface {

  private static final String USER_ID = "com.example.moderateliving.HealthActivity_USER_ID";
  private static final String TAG = "HealthActivity";
  RecyclerView recyclerView;
  HealthRecyclerAdapter mHealthRecyclerAdapter;

  ActivityHealthBinding mHealthActivityBinding;
  ModerateLivingDAO mModerateLivingDAO;
  Button mButtonHealthActivityHome;
  Button mButtonHealthActivityCreate;
  List<HealthActivities> mHealthActivities;
  int mLoggedInUserID = 0;

  //TODO: Used for Table view. May be able to delete.
  HashMap<String, int[]> tableHashMap = new HashMap<>();

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
        Intent intent = MainActivity.intentFactory(getApplicationContext(), mModerateLivingDAO.getUserByID(mLoggedInUserID).getHashPassword());
        Log.d(TAG, "Returning to Main Activity");
        startActivity(intent);
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

    //tableHashMap = initTable(); //TODO: This can be removed once table initialization is removed

    /*TABLE CODE: TODO: Do not remove until another table is created in Activity
    //Used Code as reference:
    //https://stackoverflow.com/questions/10673628/implementing-onclicklistener-for-dynamically-created-buttons-in-android
    for (String idName : tableHashMap.keySet()) {
      int instanceIDNum = 0;
      int instanceIDRow = 0;
      if(tableHashMap.get(idName) != null) {
        int[] intArray = tableHashMap.get(idName);
        assert intArray != null; //What is this??
        instanceIDNum = intArray[0];
          instanceIDRow = intArray[1];
      }
      TableRow tableView = findViewById(instanceIDRow);
      CheckBox checkBox = findViewById(instanceIDNum);
      int finalInstanceIDRow = instanceIDRow;
      checkBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mHealthActivityHome.setVisibility(View.VISIBLE);
          TableLayout healthTableLayout = mHealthActivityBinding.tableLayoutHealthActivity;
          new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
              TableRow healthTableRow = findViewById(finalInstanceIDRow);
              healthTableLayout.removeView(healthTableRow);
            }
          }.start();
        }
      });
    }*/

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
    recyclerView = findViewById(R.id.recyclerViewHealthCollection);
    mHealthActivities = mModerateLivingDAO.getHealthActivitiesByUser(mLoggedInUserID);
    mHealthRecyclerAdapter = new HealthRecyclerAdapter(this, mHealthActivities, this);
    recyclerView.setAdapter(mHealthRecyclerAdapter);
  }

  private void checkUserLoggedIn() {
    mLoggedInUserID = getIntent().getIntExtra(USER_ID, 0);
    if(mLoggedInUserID == 0) {
      Toast notLoggedIn = Toast.makeText(this, "You are not logged in. Exiting the app.", Toast.LENGTH_SHORT);
      notLoggedIn.show();
      new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }
        @Override
        public void onFinish() {
          Intent intent = MainActivity.intentFactory(getApplicationContext(), 0);
          startActivity(intent);
        }
      }.start();

    }
  }

  public HashMap<String, int[]> initTable() {
    HashMap<String, int[]> tableHashMap = new HashMap<>();
    //TableLayout healthTableLayout = mHealthActivityBinding.tableLayoutHealthActivity;
    for (UserID userID : mModerateLivingDAO.getUserIDs()) {
      String name = userID.getName();
      String weight = Double.toString(userID.getWeight());
      String birthday = userID.getBirthday();
      String idName = userID.getUsername();
      int instanceIDNum = View.generateViewId();
      int instanceIDRow = View.generateViewId();
      int[] idArray = new int[2];
      idArray[0] = instanceIDNum;
      idArray[1] = instanceIDRow;

      //Set Name row values

      TableRow healthTableRowUser = new TableRow(this);
      healthTableRowUser.setId(instanceIDRow);
      CheckBox cb = new CheckBox(getApplicationContext());
      cb.setId(instanceIDNum);
      cb.setGravity(Gravity.CENTER);
      cb.setPadding(5,5,5,0);
      tableHashMap.put(idName, idArray); //Swapped idName with idName
      healthTableRowUser.addView(cb);
      /*Section for setting Name
      TextView htxname = new TextView(this);
      htxname.setText(name);
      htxname.setTextColor(Color.BLACK);
      htxname.setGravity(Gravity.RIGHT);
      htxname.setPadding(5,5,5,0);
      htxname.setId(instanceIDNum);
      healthTableRowUser.addView(htxname);
      */
      //Set Weight row values
      TextView htxweight = new TextView(this);
      htxweight.setText(weight);
      htxweight.setTextColor(Color.BLACK);
      htxweight.setGravity(Gravity.CENTER);
      htxweight.setPadding(5,5,5,0);
      healthTableRowUser.addView(htxweight);
      //Set Birthday row values
      TextView htxbirthday = new TextView(this);
      htxbirthday.setText(birthday);
      htxbirthday.setTextColor(Color.BLACK);
      htxbirthday.setGravity(Gravity.RIGHT);
      htxbirthday.setPadding(5,5,5,0);
      healthTableRowUser.addView(htxbirthday);
      //Add row to table
      healthTableRowUser.setId(instanceIDRow);
      //healthTableLayout.addView(healthTableRowUser);
    }
    return tableHashMap;
  }

  @Override
  public void onCheckBoxSelect(HealthActivities healthActivity, boolean recreate) {
    Toast.makeText(getApplicationContext(), "Activity completed: " + healthActivity.getActivityName(), Toast.LENGTH_LONG);
    mModerateLivingDAO.delete(healthActivity);
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