package com.example.moderateliving.DB;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Converts between String and LocalDate
 */
public class LocalDateTypeConverter {
  @TypeConverter
  public String convertLocalDateToString(LocalDate date) {
    if(date != null) {
      return date.toString();
    } else {
      return null;
    }
  }

  @TypeConverter
  public LocalDate convertStringToLocalDate(String date) {
    if(date != null ) {
      String[] dateList = date.split("-");
      return LocalDate.of(Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]),
          Integer.parseInt(dateList[2]));
    } else {
      return null;
    }
  }

}
