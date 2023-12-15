package com.example.moderateliving;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.UserLog;

import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Handles RecyclerView for user log entries
 */
//TODO: Consider Inner class for LogHolder? May be good but separate class is working.
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
    logHolder.displayLog(userLogs.get(position), this);
  }

  @Override
  public int getItemCount() {
    return userLogs.size();
  }

}
