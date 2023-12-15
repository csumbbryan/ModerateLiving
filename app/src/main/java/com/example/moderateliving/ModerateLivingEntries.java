package com.example.moderateliving;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: defines methods required by any ModerateLiving entry type, like Health and Splurge
 */
public interface ModerateLivingEntries {
  String getEntryName();
  String getDescription();
  int getPoints();
  int getID();
  int getUserID();
  boolean isComplete();
}
