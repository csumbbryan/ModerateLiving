package com.example.moderateliving.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.moderateliving.TableClasses.HealthActivities;
import com.example.moderateliving.TableClasses.HealthActivityLog;
import com.example.moderateliving.TableClasses.SplurgeLog;
import com.example.moderateliving.TableClasses.Splurges;
import com.example.moderateliving.TableClasses.UserID;
import com.example.moderateliving.TableClasses.UserLog;

/**
 * @author Bryan Zanoli
 * @since 11/26/2023
 * </p>
 * Abstract: Room Database object abstraction for application
 */
@Database(entities = {UserID.class, UserLog.class, HealthActivities.class, Splurges.class, HealthActivityLog.class, SplurgeLog.class}, version = 1)
@TypeConverters(LocalDateTypeConverter.class)
public abstract class AppDataBase extends RoomDatabase {
  public static final String DATABASE_NAME = "ModerateLiving.db";
  public static final String USERID_TABLE = "UserID_table";
  public static final String USER_LOG_TABLE = "User_LOG_table";
  public static final String HEALTHACTIVITIES_TABLE = "HealthActivities_table";
  public static final String HEALTHACTIVITIES_LOG_TABLE = "HealthActivities_LOG_table";
  public static final String SPLURGES_TABLE = "Splurges_table";
  public static final String SPLURGES_LOG_TABLE = "Splurges_LOG_table";


  private static volatile AppDataBase instance;
  private static final Object LOCK = new Object();

  public abstract ModerateLivingDAO getModerateLivingDAO();

  public static AppDataBase getInstance(Context context) {
    if(instance == null) {
      synchronized (LOCK) {
        if(instance == null){
          instance = Room.databaseBuilder(context.getApplicationContext(),
          AppDataBase.class,
          DATABASE_NAME).fallbackToDestructiveMigration().build();
        }
      }
    }
    return instance;
  }

}
