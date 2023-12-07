package com.example.moderateliving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.HealthActivities;

import java.util.List;

//Source: https://www.youtube.com/watch?v=wViZsuCptt4

public class HealthRecyclerAdapter extends RecyclerView.Adapter<HealthEntryHolder> {

  View view;
  private final RecyclerViewInterface recyclerViewInterface;
  private Context context;
  private List<HealthActivities> healthEntries;

  public HealthRecyclerAdapter(Context context, List<HealthActivities> healthEntries, RecyclerViewInterface recyclerViewInterface) {
    this.context = context;
    this.healthEntries = healthEntries;
    this.recyclerViewInterface = recyclerViewInterface;
  }

  public View getView(){
    return view;
  }

  @NonNull
  @Override
  public HealthEntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_row, parent, false);
    return new HealthEntryHolder(view, recyclerViewInterface);
  }

  @Override
  public void onBindViewHolder(@NonNull HealthEntryHolder healthEntryHolder, final int position) {
    HealthActivities healthEntry = healthEntries.get(position);
    healthEntryHolder.mCheckBox.setChecked(false);
    boolean recreate = false;
    int mEntryPoints = healthEntry.getActivityPoints();
    String pointsText;
    if(mEntryPoints == 1) {
      pointsText = mEntryPoints + " Pt";
    } else {
      pointsText = mEntryPoints + " Pts";
    }
    if(healthEntryHolder != null && healthEntry != null) {
      healthEntryHolder.mActivityID = healthEntry.getActivityID();
      healthEntryHolder.mEntryDescription.setText(healthEntry.getActivityDescription());
      healthEntryHolder.mEntryName.setText(healthEntry.getActivityName());
      healthEntryHolder.mEntryPoints.setText(pointsText);
      healthEntryHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (healthEntryHolder.mCheckBox.isChecked()) {
            healthEntryHolder.isSelected = true; //TODO: review if this is needed

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            final AlertDialog alertDialog = alertBuilder.create();

            alertBuilder.setMessage("Mark Health Activity complete?\n");
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alertBuilderInner = new AlertDialog.Builder(context);
                final AlertDialog alertDialogInner = alertBuilderInner.create();

                alertBuilder.setMessage("Would you like to create a new copy of the completed Activity?\n");

                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    recyclerViewInterface.onCheckBoxSelect(healthEntry, false);
                  }
                });
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    //TODO: Call method to create new Health Activity with attributes
                    recyclerViewInterface.onCheckBoxSelect(healthEntry, true);
                    Toast.makeText(context, "Creating new Health Activity", Toast.LENGTH_LONG);
                  }
                });

                AlertDialog checkCreate = alertBuilder.create();
                checkCreate.show();
                new CountDownTimer(1000, 1000) {
                  @Override
                  public void onTick(long millisUntilFinished) {
                  }

                  @Override
                  public void onFinish() {
                    healthEntries.remove(healthEntry);
                    notifyDataSetChanged();
                  }
                }.start();
              }
            });
            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int i) {
                healthEntryHolder.mCheckBox.setChecked(false);
                notifyDataSetChanged();
              }
            });
            alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
              @Override
              public void onCancel(DialogInterface dialog) {
                healthEntryHolder.mCheckBox.setChecked(false);
                notifyDataSetChanged();
              }
            });
            AlertDialog checkDelete = alertBuilder.create();
            checkDelete.show();
          }
        }
      });
      healthEntryHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          recyclerViewInterface.onEntryLongClick(healthEntryHolder.mActivityID);
          return false;
        }
      });
    }
  }

  public void updateHealthEntryList(HealthActivities healthActivities) {
    healthEntries.add(0, healthActivities);
    notifyItemInserted(0);
  }

  @Override
  public int getItemCount() {
    return healthEntries.size();
  }
}
