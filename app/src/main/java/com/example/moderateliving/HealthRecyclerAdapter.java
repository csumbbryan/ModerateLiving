package com.example.moderateliving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.Splurges;

import java.util.List;

//Source: https://www.youtube.com/watch?v=wViZsuCptt4

public class HealthRecyclerAdapter extends RecyclerView.Adapter<EntryHolder> {

  View view;
  private final RecyclerViewInterface recyclerViewInterface;
  private Context context;
  private List<HealthActivities> healthEntries;
  private List<Splurges> splurgeEntries;
  private List<ModerateLivingEntries> mLivingEntries;

  public HealthRecyclerAdapter(Context context, List<ModerateLivingEntries> livingEntries, RecyclerViewInterface recyclerViewInterface) {
    this.context = context;
    this.mLivingEntries = livingEntries;
    //this.healthEntries = livingEntries;
    this.recyclerViewInterface = recyclerViewInterface;
  }

  public View getView(){
    return view;
  }

  @NonNull
  @Override
  public EntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_row, parent, false);
    return new EntryHolder(view, recyclerViewInterface);
  }

  @Override
  public void onBindViewHolder(@NonNull EntryHolder entryHolder, final int position) {
    //Interface Code:
    ModerateLivingEntries livingEntry = mLivingEntries.get(position);
    int darkGreen = 0xFF007800;
    int orange = 0xFFFF8800;
    if(livingEntry.getClass().equals(HealthActivities.class)) {
      entryHolder.mEntryPoints.setTextColor(darkGreen);
    } else if (livingEntry.getClass().equals(Splurges.class)) {
      entryHolder.mEntryPoints.setTextColor(orange);
    }
    int mEntryPointsI = livingEntry.getPoints();
    String pointsTextI;
    if(mEntryPointsI == 1) {
      pointsTextI = mEntryPointsI + " Pt";
    } else {
      pointsTextI = mEntryPointsI + " Pts";
    }
      entryHolder.mActivityID = livingEntry.getID();
      entryHolder.mEntryDescription.setText(livingEntry.getDescription());
      entryHolder.mEntryName.setText(livingEntry.getEntryName());
      entryHolder.mEntryPoints.setText(pointsTextI);

      /*
      //TODO: Can much of this be moved to the individual activities?
      entryHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (entryHolder.mCheckBox.isChecked()) {
            entryHolder.isSelected = true; //TODO: review if this is needed
            String messageComplete = "";
            if(livingEntry.getClass().equals(HealthActivities.class)) {
              messageComplete = "Mark Health Activity complete?\n";
            } else if (livingEntry.getClass().equals(Splurges.class)) {
              messageComplete = "Redeem Splurge?\n";
            }

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            final AlertDialog alertDialog = alertBuilder.create();

            alertBuilder.setMessage(messageComplete);
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                if(livingEntry.getClass().equals(HealthActivities.class)) {
                  String messageCopy = "Would you like to create a new copy of the completed Activity?\n";

                  AlertDialog.Builder alertBuilderInner = new AlertDialog.Builder(context);
                  final AlertDialog alertDialogInner = alertBuilderInner.create();

                  alertBuilder.setMessage(messageCopy);

                  alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      recyclerViewInterface.onCheckBoxSelect(livingEntry.getID(), false); //UPDATED TO LIVINGENTRY FROM HEALTHENTRY
                    }
                  });
                  alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //TODO: Call method to create new Health Activity with attributes
                      recyclerViewInterface.onCheckBoxSelect(livingEntry.getID(), true); //UPDATED TO LIVING ENTRY FROM HEALTHENTRY
                      Toast.makeText(context, "Creating new Health Activity", Toast.LENGTH_LONG);
                    }
                  });
                  AlertDialog checkCreate = alertBuilder.create();
                  checkCreate.show();
                } else if (livingEntry.getClass().equals(Splurges.class)) {
                  recyclerViewInterface.onCheckBoxSelect(livingEntry.getID(), false);
                }
                if(livingEntry.getClass().equals(HealthActivities.class)) {
                  new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                      mLivingEntries.remove(livingEntry); //UPDATED TO LIVING ENTRY FROM HEALTHENTRY
                      notifyDataSetChanged();
                    }
                  }.start();
                } else if(livingEntry.getClass().equals(Splurges.class)) {
                  entryHolder.mCheckBox.setChecked(false);
                }
              }
            });
            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int i) {
                if(livingEntry.getClass().equals(HealthActivities.class)) {
                  entryHolder.mCheckBox.setChecked(false);
                  notifyDataSetChanged();
                } else if (livingEntry.getClass().equals(Splurges.class)) {
                }
              }
            });
            alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
              @Override
              public void onCancel(DialogInterface dialog) {
                if(livingEntry.getClass().equals(HealthActivities.class)) {
                  entryHolder.mCheckBox.setChecked(false);
                  notifyDataSetChanged();
                } else if (livingEntry.getClass().equals(Splurges.class)) {
                }
              }
            });
            AlertDialog checkDelete = alertBuilder.create();
            checkDelete.show();
          }
        }
      });
      entryHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          recyclerViewInterface.onEntryLongClick(entryHolder.mActivityID);
          return false;
        }
      });*/
  }

  public void updateHealthEntryList(ModerateLivingEntries livingEntry) {
    mLivingEntries.add(0, livingEntry);  //UPDATED TO LIVING ENTRY FROM HEALTHENTRY
    notifyItemInserted(0);
  }

  @Override
  public int getItemCount() {
    return mLivingEntries.size();
  } //UPDATED TO LIVING ENTRY FROM HEALTHENTRY
}
