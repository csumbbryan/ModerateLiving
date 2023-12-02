package com.example.moderateliving.DB;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocalDateTypeConverter {
  @TypeConverter
  public String convertLocalDateToString(LocalDate Date) {
    return Date.toString();
  }

  @TypeConverter
  public LocalDate convertStringToLocalDate(String Date) {
    String[] DateList = Date.split("-");
    return LocalDate.of(Integer.parseInt(DateList[0]), Integer.parseInt(DateList[1]),
        Integer.parseInt(DateList[2]));
  }

}
