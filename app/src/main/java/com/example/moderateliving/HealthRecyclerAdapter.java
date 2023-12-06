package com.example.moderateliving;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.HealthActivities;

import java.util.ArrayList;
import java.util.List;

//Source: https://www.youtube.com/watch?v=wViZsuCptt4

public class HealthRecyclerAdapter extends RecyclerView.Adapter<HealthEntryHolder> {

  private Context context;
  private List<HealthActivities> healthEntries = new ArrayList<>();

  public HealthRecyclerAdapter(Context context, List<HealthActivities> healthEntries) {
    this.context = context;
    this.healthEntries = healthEntries;
  }

  @NonNull
  @Override
  public HealthEntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_row, parent, false);
    return new HealthEntryHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull HealthEntryHolder healthEntryHolder, final int position) {
    HealthActivities healthEntry = healthEntries.get(position);
    int mEntryPoints = healthEntry.getActivityPoints();
    String pointsText;
    if(mEntryPoints == 1) {
      pointsText = mEntryPoints + " Pt";
    } else {
      pointsText = mEntryPoints + " Pts";
    }
    if(healthEntryHolder != null && healthEntry != null) {
      try {
        healthEntryHolder.mEntryDescription.setText(healthEntry.getActivityDescription());
        healthEntryHolder.mEntryName.setText(healthEntry.getActivityName());
        healthEntryHolder.mEntryPoints.setText(pointsText);
      } catch (NullPointerException e) {
        Toast loginError = Toast.makeText(context, "Null Pointer Exception", Toast.LENGTH_LONG);
        loginError.show();
      }
    }

  }

  @Override
  public int getItemCount() {
    return healthEntries.size();
  }
}
