package com.example.moderateliving;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.LogRecyclerViewInterface;
import com.example.moderateliving.TableClasses.UserLog;

import java.time.LocalDate;
import java.util.List;

//TODO: Consider Inner class for LogHolder?
public class LogRecyclerAdapter extends RecyclerView.Adapter<LogHolder> {

  private View view;
  private Context context;

  private List<UserLog> userLogs;
  private int selectedItemPosition = RecyclerView.NO_POSITION;
  private LogRecyclerViewInterface mLogRecyclerViewInterface;

  public LogRecyclerAdapter(Context context, List<UserLog> userLogs, LogRecyclerViewInterface logRecyclerViewInterface) {
    this.context = context;
    this.userLogs = userLogs;
    this.mLogRecyclerViewInterface = logRecyclerViewInterface;
  }

  @NonNull
  @Override
  public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_row, parent, false);
    return new LogHolder(view, mLogRecyclerViewInterface);
  }

  @Override
  public void onBindViewHolder(@NonNull LogHolder logHolder, int position) {
    UserLog userLog = userLogs.get(position);
    int mPoints = userLog.getPoints();
    String pointsText;
    String forText = "for";
    String withText = "with";
    if(mPoints == 1) {
      pointsText = mPoints + " Pt";
    } else {
      pointsText = mPoints + " Pts";
    }
    String forWith;
    if(userLog.getDescription().equals(Util.LOG_CREATED)) {
      logHolder.mForWith.setText(withText);
    } else {
      logHolder.mForWith.setText(forText);
    }
    logHolder.mUsername.setText(userLog.getUsername());
    logHolder.mActivityType.setText(userLog.getActivityType());
    logHolder.mDate.setText(userLog.getTransactionDate().toString());
    logHolder.mDescription.setText(userLog.getDescription());
    logHolder.mPoints.setText(pointsText);
    if(selectedItemPosition == RecyclerView.NO_POSITION) {
      logHolder.itemView.setBackgroundColor(0xFFFFFFFF);
    } else {
      if (selectedItemPosition == logHolder.getAbsoluteAdapterPosition()) {
        logHolder.itemView.setBackgroundColor(0xAAEEEEEE);
      } else {
        logHolder.itemView.setBackgroundColor(0xFFFFFFFF);
      }
    }
    logHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        logHolder.itemView.setBackgroundColor(0xAAEEEEEE);
        logHolder.isSelected = true;
        if(selectedItemPosition != logHolder.getAbsoluteAdapterPosition()) {
          notifyItemChanged(selectedItemPosition);
          selectedItemPosition = logHolder.getAbsoluteAdapterPosition();
          logHolder.isSelected = false;
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return userLogs.size();
  }
}
