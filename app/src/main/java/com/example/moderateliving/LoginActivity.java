package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.databinding.ActivityLoginBinding;
import com.example.moderateliving.databinding.ActivityMainBinding;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

  private static final String USER_PASSWORD_HASH = "com.example.moderateliving_Login_Activity";
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

    mUserNameInput = mActivityLoginBinding.editTextUserInput;
    mPasswordInput = mActivityLoginBinding.editTextPasswordInput;
    mLoginSelect = mActivityLoginBinding.buttonLoginSelect;
    mSignUpSelect = mActivityLoginBinding.buttonSignUpSelect;

    mLoginSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        submitUserCredentials();
        Intent intent = MainActivity.intentFactory(getApplicationContext(), mUserID.getHashPassword());
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
    boolean test = Util.verifyCredentials(username,password);


  }
}