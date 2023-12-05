package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityHealthBinding;

import java.util.HashMap;


public class HealthActivity extends AppCompatActivity {

  ActivityHealthBinding mHealthActivityBinding;
  ModerateLivingDAO mModerateLivingDAO;
  Button mHealthActivityTest;

  HashMap<Integer, String> tableHashMap = new HashMap<>();

  public static Intent intentFactory(Context packageContext) {
    Intent intent = new Intent(packageContext, HealthActivity.class);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health);

    mHealthActivityBinding = ActivityHealthBinding.inflate(getLayoutInflater());
    setContentView(mHealthActivityBinding.getRoot());

    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();

    mHealthActivityTest = mHealthActivityBinding.TESTbuttonHealthActivity;
    tableHashMap = initTable();

    //Used Code as reference:
    //https://stackoverflow.com/questions/10673628/implementing-onclicklistener-for-dynamically-created-buttons-in-android
    for (Integer i : tableHashMap.keySet()) {
      TextView textView = findViewById(i);
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mHealthActivityTest.setVisibility(View.VISIBLE);
        }
      });
    }

  }

  //TODO: Update below to accommodate Health Activity Records Instead
  public HashMap<Integer, String> initTable() {
    HashMap<Integer, String> tableHashMap = new HashMap<>();
    TableLayout healthTableLayout = mHealthActivityBinding.healthTableLayout2;
    for (UserID userID : mModerateLivingDAO.getUserIDs()) {
      String name = userID.getName();
      String weight = Double.toString(userID.getWeight());
      String birthday = userID.getBirthday();
      String idName = userID.getUsername();
      int instanceIDNum = View.generateViewId();
      tableHashMap.put(instanceIDNum, idName);
      //Set Name row values
      TableRow healthTableRowUser = new TableRow(this);
      TextView htxname = new TextView(this);
      htxname.setText(name);
      htxname.setTextColor(Color.BLACK);
      htxname.setGravity(Gravity.CENTER);
      htxname.setId(instanceIDNum);
      healthTableRowUser.addView(htxname);
      //Set Weight row values
      TextView htxweight = new TextView(this);
      htxweight.setText(weight);
      htxweight.setTextColor(Color.BLACK);
      htxweight.setGravity(Gravity.CENTER);
      healthTableRowUser.addView(htxweight);
      //Set Birthday row values
      TextView htxbirthday = new TextView(this);
      htxbirthday.setText(birthday);
      htxbirthday.setTextColor(Color.BLACK);
      htxbirthday.setGravity(Gravity.CENTER);
      healthTableRowUser.addView(htxbirthday);
      //Add row to table
      healthTableLayout.addView(healthTableRowUser);
    }
    return tableHashMap;
  }
}