package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.databinding.ActivityAdminBinding;
import com.example.moderateliving.databinding.ActivityMainBinding;

public class AdminActivity extends AppCompatActivity {

  public static final String TAG = "AdminActivity";
  private static final String SHARED_PREF_STRING = "com.example.moderateliving_SHARED_PREF_STRING";

  private Button mButtonDatabaseReset;
  private Button mButtonUserManagement;
  private ImageButton mButtonAdminClose;
  private ModerateLivingDAO mModerateLivingDAO;

  ActivityAdminBinding mActivityAdminBinding;
  public static Intent intentFactory(Context packageContext) {
    Intent intent = new Intent(packageContext, AdminActivity.class);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    mActivityAdminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
    setContentView(mActivityAdminBinding.getRoot());

    mButtonDatabaseReset = mActivityAdminBinding.buttonAdminActivityDatabaseReset;
    mButtonAdminClose = mActivityAdminBinding.buttonAdminClose;
    mButtonUserManagement = mActivityAdminBinding.buttonAdminActivityUserManagement;

    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();

    mButtonAdminClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SharedPreferences loginSharedPref = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor loginSharedEditor = loginSharedPref.edit();
        loginSharedEditor.remove(SHARED_PREF_STRING);
        loginSharedEditor.apply();
        //LoggedInToken.logUserOut(); //TODO: review for LoggedInToken need
        finish();
        Intent intent = MainActivity.intentFactory(getApplicationContext(), -1);
        startActivity(intent);
        //CALL: Close App
      }
    });

    mButtonUserManagement.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = UserManagementActivity.intentFactory(getApplicationContext());
        startActivity(intent);
      }
    });
  }
}