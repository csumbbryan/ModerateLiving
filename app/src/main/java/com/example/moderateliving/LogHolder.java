package com.example.moderateliving;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.LogRecyclerViewInterface;

import java.time.LocalDate;

public class LogHolder extends RecyclerView.ViewHolder{

  private static int mItemID;
  private static int mUserID;


  public TextView mUsername;
  public TextView mDescription;
  public TextView mActivityType;
  public TextView mDate;
  public TextView mForWith;
  public TextView mPoints;
  public boolean isSelected;

  public LogHolder(@NonNull View itemView, LogRecyclerViewInterface mLogRecyclerViewInterface) {
    super(itemView);
    mUsername = itemView.findViewById(R.id.textViewLogEntryUsernameDisplay);
    mDescription = itemView.findViewById(R.id.textViewLogEntryDescription);
    mActivityType = itemView.findViewById(R.id.textViewLogEntryActivityType);
    mDate = itemView.findViewById(R.id.textViewLogEntryDate);
    mForWith = itemView.findViewById(R.id.textViewLogEntryForWith);
    mPoints = itemView.findViewById(R.id.textViewLogEntryPoints);
    isSelected = false;


  }


}
