package com.example.moderateliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

  public static Intent intentFactory(Context packageContext) {
    Intent intent = new Intent(packageContext, SignUpActivity.class);
    return intent;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
  }
}