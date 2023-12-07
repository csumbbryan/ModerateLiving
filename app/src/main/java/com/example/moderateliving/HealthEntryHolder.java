package com.example.moderateliving;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class HealthEntryHolder extends RecyclerView.ViewHolder {

  public CheckBox mCheckBox;
  public TextView mEntryName;
  public TextView mEntryDescription;
  public TextView mEntryPoints;
  public Integer mActivityID; //TODO: is needed?
  public boolean isSelected;
  public HealthEntryHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
    super(itemView);
    mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxIsIncomplete);
    mEntryName = (TextView) itemView.findViewById(R.id.textViewHealthEntryName);
    mEntryDescription = (TextView) itemView.findViewById(R.id.textViewHealthEntryDescription);
    mEntryPoints = (TextView) itemView.findViewById(R.id.textViewHealthEntryPoints);


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