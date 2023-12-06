package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityHealthBinding;

import java.util.HashMap;


public class HealthActivity extends AppCompatActivity {

  ActivityHealthBinding mHealthActivityBinding;
  ModerateLivingDAO mModerateLivingDAO;
  Button mHealthActivityHome;

  HashMap<String, int[]> tableHashMap = new HashMap<>();

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

    mHealthActivityHome = mHealthActivityBinding.buttonHealthActivityHome;
    tableHashMap = initTable();

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
    }

  }

  //TODO: Update below to accommodate Health Activity Records Instead
  public HashMap<String, int[]> initTable() {
    HashMap<String, int[]> tableHashMap = new HashMap<>();
    TableLayout healthTableLayout = mHealthActivityBinding.tableLayoutHealthActivity;
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
      healthTableLayout.addView(healthTableRowUser);
    }
    return tableHashMap;
  }
}