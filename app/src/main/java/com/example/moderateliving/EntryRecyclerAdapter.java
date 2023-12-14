package com.example.moderateliving;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.Splurges;

import java.util.List;


//TODO: Consider inner class for EntryHolder?
//Source: https://www.youtube.com/watch?v=wViZsuCptt4
public class EntryRecyclerAdapter extends RecyclerView.Adapter<EntryHolder> {

  View view;
  private final RecyclerViewInterface recyclerViewInterface;
  private Context context;
  private List<ModerateLivingEntries> mLivingEntries;

  public EntryRecyclerAdapter(Context context, List<ModerateLivingEntries> livingEntries, RecyclerViewInterface recyclerViewInterface) {
    this.context = context;
    this.mLivingEntries = livingEntries;
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
    entryHolder.mCheckBox.setChecked(livingEntry.isComplete());
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
