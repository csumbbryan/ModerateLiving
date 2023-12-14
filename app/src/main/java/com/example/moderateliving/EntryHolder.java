package com.example.moderateliving;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class EntryHolder extends RecyclerView.ViewHolder {

  public CheckBox mCheckBox;
  public TextView mEntryName;
  public TextView mEntryDescription;
  public TextView mEntryPoints;
  public Integer mActivityID;
  public boolean isSelected; //TODO: is this needed?
  public EntryHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
    super(itemView);
    //TODO: how can we handle the onClick better?
    mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxIsIncomplete);
    mEntryName = (TextView) itemView.findViewById(R.id.textViewEntryName);
    mEntryDescription = (TextView) itemView.findViewById(R.id.textViewEntryDescription);
    mEntryPoints = (TextView) itemView.findViewById(R.id.textViewEntryPoints);



    mCheckBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(recyclerViewInterface != null) {
          if(mCheckBox.isChecked()) {
            int position = getBindingAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
              recyclerViewInterface.onCheckBoxSelect(position);
              mCheckBox.setChecked(false);
            }
          }
        }
      }
    });

    itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if(recyclerViewInterface != null) {
          int position = getBindingAdapterPosition();
          if(position != RecyclerView.NO_POSITION) {
            recyclerViewInterface.onEntryLongClick(position);
          }
        }
        return false;
      }
    });
  }
}