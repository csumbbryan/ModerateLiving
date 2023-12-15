package com.example.moderateliving;

import com.example.moderateliving.TableClasses.HealthActivities;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: defines methods needed for interfacing with Recycler view of ModerateEntry type
 */
//Source: https://www.youtube.com/watch?v=7GPUpvcU1FE&list=PLcSIMAULmMycPczycWjgHQjBPiWAkrwT2&index=2
public interface RecyclerViewInterface {
  void onCheckBoxSelect(int position);
  void onEntryLongClick(int position);
}
