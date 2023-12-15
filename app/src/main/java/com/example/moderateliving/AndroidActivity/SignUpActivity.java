package com.example.moderateliving.AndroidActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderateliving.DB.AppDataBase;
import com.example.moderateliving.DB.ModerateLivingDAO;
import com.example.moderateliving.MainActivity;
import com.example.moderateliving.R;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.Util;
import com.example.moderateliving.databinding.ActivitySignUpBinding;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Bryan Zanoli
 * @since December 5, 2023
 * </p>
 * Abstract: activity for user sign up page.
 */
public class SignUpActivity extends AppCompatActivity {

  private static final String SHARED_PREF_STRING = "com.example.moderateliving_SHARED_PREF_STRING";
  private static final String TAG = "Sign Up Activity";
  //Source: https://stackoverflow.com/questions/22061723/regex-date-validation-for-yyyy-mm-dd
  private static final String REGEX_DATE = "\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])";
  private static final String USER_ID = "com.example.moderateliving.SignUpActivity_USER_ID";
  private static final int NO_USER = 0;
  ActivitySignUpBinding mSignUpBinding;
  ModerateLivingDAO mModerateLivingDAO;

  Button mButtonSignUpSumbit;
  EditText mEditTextSignUpFullName;
  EditText mEditTextPasswordSignUp1;
  EditText mEditTextPasswordSignUp2;
  EditText mEditTextUserSignUp;
  EditText mEditTextSignUpWeight;
  EditText mEditTextSignUpBirthday;
  TextView mTextViewMainText;
  CheckBox mCheckBoxSignUpIsAdmin;
  Button mButtonSignUpDiscard;

  String mUsername;
  String mFullname;
  String mPassword1;
  String mPassword2;
  Double mWeight;
  String mBirthday;
  int mLoggedInUserID;
  int mUserID;
  boolean mUpdateUser;


  public static Intent intentFactory(Context packageContext, int mUserID) {
    Intent intent = new Intent(packageContext, SignUpActivity.class);
    intent.putExtra(USER_ID, mUserID);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    mSignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
    setContentView(mSignUpBinding.getRoot());

    getDatabase();
    checkUserLoggedIn();
    initializeBindings();
    checkForUpdateUser();
    refreshDisplay();



    mButtonSignUpSumbit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFormValues();
        if(mUpdateUser) {
          submitFormExisting();
        } else {
          submitFormNew();
        }
      }
    });

    mButtonSignUpDiscard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnToPreviousActivity();
      }
    });

  }

  private void checkUserLoggedIn() {
    SharedPreferences loginSharedPref = getSharedPreferences(SHARED_PREF_STRING, Context.MODE_PRIVATE);
    List<UserID> mUserIDList = mModerateLivingDAO.getUserIDs();
    int loggedInUserHash = loginSharedPref.getInt(SHARED_PREF_STRING, NO_USER);
    if(loggedInUserHash == 0) {
      mLoggedInUserID = 0;
    } else {
      mLoggedInUserID = Util.findUserByHash(mUserIDList, loggedInUserHash).getUserID();
    }
  }

  private void initializeBindings() {
    mButtonSignUpSumbit = mSignUpBinding.buttonSignUpSubmit;
    mEditTextUserSignUp = mSignUpBinding.editTextUserSignUp;
    mEditTextSignUpFullName = mSignUpBinding.editTextSignUpFullNameInput;
    mEditTextPasswordSignUp1 = mSignUpBinding.editTextPasswordSignUp1;
    mEditTextPasswordSignUp2 = mSignUpBinding.editTextPasswordSignUp2;
    mEditTextSignUpWeight = mSignUpBinding.editTextSignUpWeightInput;
    mEditTextSignUpBirthday = mSignUpBinding.editTextSignUpBirthdayInput;
    mTextViewMainText = mSignUpBinding.textViewSignUpText;
    mCheckBoxSignUpIsAdmin = mSignUpBinding.checkBoxSignUpIsAdmin;
    mButtonSignUpDiscard = mSignUpBinding.buttonSignUpDiscard;
  }



  private void refreshDisplay() {
    if(mLoggedInUserID > 0) {
      if(mUpdateUser) {
        mEditTextUserSignUp.setBackgroundColor(getResources().getColor(R.color.grayExtraDark, null));
        mEditTextUserSignUp.setTextColor(getResources().getColor(R.color.white, null));
        mEditTextUserSignUp.setText(mModerateLivingDAO.getUserByID(mUserID).getUsername());
        mEditTextSignUpFullName.setText(mModerateLivingDAO.getUserByID(mUserID).getName());
        mEditTextSignUpBirthday.setText(mModerateLivingDAO.getUserByID(mUserID).getBirthday());
        mEditTextSignUpWeight.setText(String.valueOf(mModerateLivingDAO.getUserByID(mUserID).getWeight()));
        mEditTextPasswordSignUp1.setVisibility(EditText.GONE);
        mEditTextPasswordSignUp2.setVisibility(EditText.GONE);
        mButtonSignUpSumbit.setText(getResources().getText(R.string.stringSignUpSubmit));
        mTextViewMainText.setText(getResources().getText(R.string.stringSignUpUpdate));
        mButtonSignUpDiscard.setVisibility(Button.VISIBLE);
      } else {
        mTextViewMainText.setText(getResources().getText(R.string.stringSignUpNewUser));
        mButtonSignUpDiscard.setVisibility(Button.VISIBLE);
      }
      if(mModerateLivingDAO.getUserByID(mLoggedInUserID).getIsAdmin()) {
        mCheckBoxSignUpIsAdmin.setVisibility(CheckBox.VISIBLE);
        if(mUpdateUser) {
          mCheckBoxSignUpIsAdmin.setChecked(mModerateLivingDAO.getUserByID(mUserID).getIsAdmin());
        }
      }
    }
  }

  private boolean checkForUpdateUser() {
    mUserID = getIntent().getIntExtra(USER_ID, NO_USER);
    if(mUserID > 0) {
      mUpdateUser = true;
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    mModerateLivingDAO = null;
    finish();
  }

  /**
   * Obtain form values from EditText fields.
   * Retrieves EditText field values and sets the to the corresponding String and number values.
   */
  private void getFormValues() {
    mUsername = mEditTextUserSignUp.getText().toString();
    mFullname = mEditTextSignUpFullName.getText().toString();
    mPassword1 = mEditTextPasswordSignUp1.getText().toString();
    mPassword2 = mEditTextPasswordSignUp2.getText().toString();
    try {
      mWeight = Double.parseDouble(mEditTextSignUpWeight.getText().toString());
    } catch (NumberFormatException e) {
      Log.d(TAG, "Could not parse weight of value: " + mEditTextSignUpWeight.getText().toString());
    }
    mBirthday = mEditTextSignUpBirthday.getText().toString();
  }

  private void submitFormExisting() {
    UserID existingUser = mModerateLivingDAO.getUserByID(mUserID);
    if(checkPasswordMatch() && checkBirthdayFormat()) {
      existingUser.setName(mFullname);
      existingUser.setBirthday(mBirthday);
      existingUser.setWeight(mWeight);
      if(mModerateLivingDAO.getUserByID(mLoggedInUserID).getIsAdmin()) {
        existingUser.setIsAdmin(mCheckBoxSignUpIsAdmin.isChecked());
      }
      mModerateLivingDAO.update(existingUser);
      returnToPreviousActivity();
    } else {
      refreshDisplay();
    }

  }

  private void returnToPreviousActivity() {
    finish();
  }

  /**
   * Validates data input and inserts User into UserID table.
   * Verifies the username does not exist. Verifies the passwords match. Verifies the Birthday date
   * format is correct. Inserts value into UserID database and logs said insertion. Switches to main
   * activity afterwards, passing in the userPassHash for use as the logged in user.
   */
  private void submitFormNew() {
    int userPoints = 0;
    boolean isAdmin = false;
    if(mLoggedInUserID > 0 && mModerateLivingDAO.getUserByID(mLoggedInUserID).getIsAdmin()) {
      isAdmin = mCheckBoxSignUpIsAdmin.isChecked();
    }

    if(checkUsername() && checkPasswordMatch() && checkBirthdayFormat()) {
      UserID newUser = new UserID(
          mUsername,
          mFullname,
          userPoints,
          mPassword1,
          isAdmin,
          mWeight,
          mBirthday);
      mModerateLivingDAO.insert(newUser);

      Log.d(TAG, "New user created: " +
          "\nUsername: " + mUsername +
          "\nFullname: " + mFullname +
          "\nWeight: " + mWeight +
          "\nBirthday: " + mBirthday);
      Log.d(TAG, "Switching to Main Activity");
      if(mLoggedInUserID > 0) {
        returnToPreviousActivity();
      } else {
        Intent intent = MainActivity.intentFactory(getApplicationContext(), newUser.getHashPassword());
        startActivity(intent);
      }
    } else {
      clearDisplay();
    }
  }

  /**
   * Checks the Birthday format.
   * @return whether or not input passes validation
   */
  private boolean checkBirthdayFormat() {
    //Source for Pattern code: https://stackoverflow.com/questions/39516774/require-user-input-to-follow-string-format
    Pattern datePattern = Pattern.compile(REGEX_DATE);
    if(!datePattern.matcher(mBirthday).find()) {
      Log.d(TAG, "Incorrectly formatted birthday for " + mUsername + ".");
      Toast loginError = Toast.makeText(getApplicationContext(),
          "Birthday is not formatted correctly. Please resubmit the form.", Toast.LENGTH_LONG);
      loginError.show();
      return false;
    }
    else {return true;}
  }

  /**
   * Checks passwords match
   * @return whether or not passwords both match
   */
  private boolean checkPasswordMatch() {
    if(!mPassword1.equals(mPassword2)) {
      Log.d(TAG, "New User creation attempted for " + mUsername + ". However, passwords do not match");
      Toast loginError = Toast.makeText(getApplicationContext(),
          "Passwords do not match. Please resubmit the form.", Toast.LENGTH_LONG);
      loginError.show();
      return false;
    }
    else {
      return true;
    }
  }

  /**
   * Checks if user already exists in UserID database
   * @return whether or not user already exists
   */
  private boolean checkUsername() {
    if(mModerateLivingDAO.getUserByUsername(mUsername) != null) {
      if (mUsername.equals(mModerateLivingDAO.getUserByUsername(mUsername).getUsername())) {
        Log.d(TAG, "New user creation was attempted but user " + mUsername + " already exists.");
        Toast loginError = Toast.makeText(getApplicationContext(),
            "Username already exists. Please select a new username and resubmit the form.", Toast.LENGTH_LONG);
        loginError.show();
        return false;
      }
    }
    return true;
  }

  /**
   * Initializes Data Access Object
   */
  private void getDatabase() {
    mModerateLivingDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
        .allowMainThreadQueries()
        .build().
        getModerateLivingDAO();
  }

  /**
   * Clears the Sign Up fields for re-entry
   */
  private void clearDisplay() {
    mEditTextUserSignUp.setText("");
    mEditTextSignUpFullName.setText("");
    mEditTextPasswordSignUp1.setText("");
    mEditTextPasswordSignUp2.setText("");
    mEditTextSignUpWeight.setText("");
    mEditTextSignUpBirthday.setText("");
  }
}