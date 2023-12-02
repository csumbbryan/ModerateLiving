package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityLoginBinding;
import com.example.moderateliving.databinding.ActivityMainBinding;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

  private static final String USER_PASSWORD_HASH = "com.example.moderateliving_Login_Activity";
  private static final String TAG = "LoginActivity";
  ActivityLoginBinding mActivityLoginBinding;
  EditText mUserNameInput;
  EditText mPasswordInput;
  Button mLoginSelect;
  Button mSignUpSelect;

  ModerateLivingDAO mModerateLivingDAO;

  List<UserID> mUserIDList;
  UserID mUserID;

  public static Intent intentFactory(Context packageContext, Integer userHash) {
    Intent intent = new Intent(packageContext, LoginActivity.class);
    intent.putExtra(USER_PASSWORD_HASH, userHash);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mActivityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(mActivityLoginBinding.getRoot());

    //TODO: need to set input fields to have text disappear when typing begins
    //TODO: need to set password field to have *** input
    mUserNameInput = mActivityLoginBinding.editTextUserInput;
    mPasswordInput = mActivityLoginBinding.editTextPasswordInput;
    mLoginSelect = mActivityLoginBinding.buttonLoginSelect;
    mSignUpSelect = mActivityLoginBinding.buttonSignUpSelect;

    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();

    mLoginSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        submitUserCredentials(); //TODO: Update submitUserCredentials
        Integer userHash = 0;
        if(mUserID != null) {
          userHash = mUserID.getHashPassword();
        }
        mUserNameInput.setText("");
        mPasswordInput.setText("");
        Intent intent = MainActivity.intentFactory(getApplicationContext(), userHash);
        Log.d(TAG, "Switching to MainActivity View");
        startActivity(intent);
      }
    });

    mSignUpSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: setup signup methods and functionality

      }
    });
  }

  //TODO: Complete method below
  private void submitUserCredentials() {
    String username = mUserNameInput.getText().toString();
    String password = mPasswordInput.getText().toString();
    boolean test = Util.verifyCredentials(mModerateLivingDAO.getUserIDs(), username ,password);


  }
}