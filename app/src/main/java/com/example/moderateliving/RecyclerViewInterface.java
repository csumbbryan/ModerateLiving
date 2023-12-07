package com.example.moderateliving;

import com.example.moderateliving.TableClasses.HealthActivities;

//Source: https://www.youtube.com/watch?v=7GPUpvcU1FE&list=PLcSIMAULmMycPczycWjgHQjBPiWAkrwT2&index=2
public interface RecyclerViewInterface {
  void onCheckBoxSelect(HealthActivities healthEntry, boolean recreate);
  void onEntryLongClick(int position);
}
