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
import com.example.moderateliving.databinding.ActivityUserManagementBinding;

import java.util.HashMap;

public class UserManagementActivity extends AppCompatActivity {

  private ActivityUserManagementBinding mActivityUserManagementBinding;
  private ModerateLivingDAO mModerateLivingDAO;
  private HashMap<Integer, String> tableHashMap = new HashMap<>();

  private Button mButtonUserManagement;

  //TODO: Modify to be used with Fragments and Recycler view
  //TODO: Functionality: include ability to add new user with admin rights
  //TODO: Functionality: include ability to grant admin rights to user
  //TODO: Functionality: include ability to delete user
  //TODO: Functionality: include ability to reset user password
  //TODO: onClick for Return Home

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_management);

    mActivityUserManagementBinding = ActivityUserManagementBinding.inflate(getLayoutInflater());
    setContentView(mActivityUserManagementBinding.getRoot());
    mButtonUserManagement = mActivityUserManagementBinding.buttonUserManagement;

    getDatabase();

    initTable();

    //Used Code as reference:
    //https://stackoverflow.com/questions/10673628/implementing-onclicklistener-for-dynamically-created-buttons-in-android
    for (Integer i : tableHashMap.keySet()) {
      TextView textView = findViewById(i);
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mButtonUserManagement.setVisibility(View.VISIBLE);
        }
      });
    }


  }

  public static Intent intentFactory(Context packageContext) {
    Intent intent = new Intent(packageContext, UserManagementActivity.class);
    return intent;
  }

  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  public void initTable() {
    TableLayout userManagementTableLayout = mActivityUserManagementBinding.userManagementTableLayout;
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
      userManagementTableLayout.addView(healthTableRowUser);
    }
  }
}