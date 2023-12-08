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
  public Integer mActivityID; //TODO: is needed?
  public boolean isSelected;
  public EntryHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
    super(itemView);
    //TODO: how can we do this better?
    mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxIsIncomplete);
    mEntryName = (TextView) itemView.findViewById(R.id.textViewEntryName);
    mEntryDescription = (TextView) itemView.findViewById(R.id.textViewEntryDescription);
    mEntryPoints = (TextView) itemView.findViewById(R.id.textViewEntryPoints);


    /*
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(recyclerViewInterface != null) {
          int position = getBindingAdapterPosition();
          if(position != RecyclerView.NO_POSITION) {
            recyclerViewInterface.onEntryClick(position);
          }
        }
      }
    });*/
  }
}