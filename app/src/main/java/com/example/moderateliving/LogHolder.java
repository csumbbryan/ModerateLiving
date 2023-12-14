package com.example.moderateliving;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.UserLog;

import java.util.List;

public class LogHolder extends RecyclerView.ViewHolder{

  public static final int DEFAULT_COLOR = 0xFFFFFFFF;
  public static final int SELECTED_COLOR = 0xAAEEEEEE;
  public TextView mUsername;
  public TextView mDescription;
  public TextView mName;
  public TextView mActivityType;
  public TextView mDate;
  public TextView mForWith;
  public TextView mPoints;
  public int mBackgroundColor;
  public boolean isSelected;
  private LogRecyclerViewInterface mLogRecyclerViewInterface;
  public static int selectedItemPosition = RecyclerView.NO_POSITION;

  public LogHolder(@NonNull View itemView, LogRecyclerViewInterface mLogRecyclerViewInterface) {
    super(itemView);
    this.mLogRecyclerViewInterface = mLogRecyclerViewInterface;
    mUsername = itemView.findViewById(R.id.textViewLogEntryUsernameDisplay);
    mDescription = itemView.findViewById(R.id.textViewLogEntryDescription);
    mName = itemView.findViewById(R.id.textViewLogTypeName);
    mActivityType = itemView.findViewById(R.id.textViewLogEntryActivityType);
    mDate = itemView.findViewById(R.id.textViewLogEntryDate);
    mForWith = itemView.findViewById(R.id.textViewLogEntryForWith);
    mPoints = itemView.findViewById(R.id.textViewLogEntryPoints);
    mBackgroundColor = DEFAULT_COLOR;
    isSelected = false;
  }

  public void displayLog(UserLog userLog, LogRecyclerAdapter logRecyclerAdapter) {
    if(mLogRecyclerViewInterface != null && logRecyclerAdapter != null) {
      int mPoints = userLog.getPoints();
      String pointsText;
      String forText = "for";
      String withText = "with";
      if (mPoints == 1) {
        pointsText = mPoints + " Pt";
      } else {
        pointsText = mPoints + " Pts";
      }
      String forWith;
      if (userLog.getDescription().equals(Util.LOG_CREATED)) {
        this.mForWith.setText(withText);
      } else {
        this.mForWith.setText(forText);
      }
      this.itemView.setBackgroundColor(mBackgroundColor);
      this.mUsername.setText(userLog.getUsername());
      this.mActivityType.setText(userLog.getActivityType());
      this.mDate.setText(userLog.getTransactionDate().toString());
      this.mDescription.setText(userLog.getDescription());
      this.mName.setText(userLog.getLogEntryName());
      this.mPoints.setText(pointsText);
      if (selectedItemPosition == RecyclerView.NO_POSITION) {
        //mBackgroundColor = DEFAULT_COLOR;
        this.itemView.setBackgroundColor(0xFFFFFFFF);
      } else {
        if (selectedItemPosition == this.getAbsoluteAdapterPosition()) {
          //mBackgroundColor = SELECTED_COLOR;
          this.itemView.setBackgroundColor(0xAAEEEEEE);
          mLogRecyclerViewInterface.clickedSelected(selectedItemPosition);
        } else {
          //mBackgroundColor = DEFAULT_COLOR;
          this.itemView.setBackgroundColor(0xFFFFFFFF);
        }
      }
    }

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //logHolder.mBackgroundColor = LogHolder.SELECTED_COLOR;
        itemView.setBackgroundColor(LogHolder.SELECTED_COLOR);
        isSelected = true;
        logRecyclerAdapter.notifyItemChanged(getAbsoluteAdapterPosition());
        if(LogHolder.selectedItemPosition != getAbsoluteAdapterPosition()) {
          //logHolder.mBackgroundColor = LogHolder.DEFAULT_COLOR;
          itemView.setBackgroundColor(LogHolder.DEFAULT_COLOR);
          logRecyclerAdapter.notifyItemChanged(LogHolder.selectedItemPosition);
          LogHolder.selectedItemPosition = getAbsoluteAdapterPosition();
          isSelected = false;
        }
        //notifyItemChanged(selectedItemPosition);
      }
    });
  }


}
