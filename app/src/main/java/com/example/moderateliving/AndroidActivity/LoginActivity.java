package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivityLoginBinding;

import java.util.List;
import java.util.Objects;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: User login Activity View.
 * Checks whether username and password match entry in database before allowing login.
 */
public class LoginActivity extends AppCompatActivity {

  private static final String USER_PASSWORD_HASH = "com.example.moderateliving_Login_Activity";
  private static final String SHARED_PREF_STRING = "com.example.moderateliving_SHARED_PREF_STRING";
  private static final int NO_USER = 0;
  private static final String TAG = "LoginActivity";
  ActivityLoginBinding mActivityLoginBinding;
  EditText mUserNameInput;
  EditText mPasswordInput;
  Button mLoginSelect;
  Button mSignUpSelect;

  ModerateLivingDAO mModerateLivingDAO;

  List<UserID> mUserIDList;
  UserID mUser;

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

    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();

    mLoginSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        boolean isLoginSuccessful = submitUserCredentials();
        if(isLoginSuccessful && mUser != null) {
          Intent intent = MainActivity.intentFactory(getApplicationContext(), mUser.getHashPassword());
          mUserNameInput.setText("");
          mPasswordInput.setText("");
          Log.d(TAG, "Switching to MainActivity View");
          startActivity(intent);
        } else {
            Toast loginError = Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_LONG);
            loginError.show();
        }
      }
    });

    mSignUpSelect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = SignUpActivity.intentFactory(getApplicationContext(), NO_USER);
        startActivity(intent);
      }
    });
  }

  private boolean submitUserCredentials() {
    String username = mUserNameInput.getText().toString();
    int userHash = Objects.hash(username, mPasswordInput.getText().toString());
    boolean isLoginSuccessful = Util.verifyCredentials(mModerateLivingDAO.getUserIDs(), userHash);
    if(isLoginSuccessful) {
      mUser = mModerateLivingDAO.getUserByUsername(username);
      SharedPreferences loginPreference = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
      SharedPreferences.Editor loginEdit = loginPreference.edit();
      loginEdit.putInt(SHARED_PREF_STRING, userHash);
      loginEdit.apply();
      return true;
    }
    return false;
  }
}