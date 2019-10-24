package com.wifi.wifiscanner.da;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wifi.wifiscanner.dto.ReportEntity;

import java.util.List;

@Dao
public interface ReportDao {

  @Query("SELECT * from reportentity")
  List<ReportEntity> getAll();

  @Query("SELECT * from reportentity where id = :reportId")
  ReportEntity getById(long reportId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(ReportEntity report);

  @Update
  void update(ReportEntity report);

  @Delete
  void delete(ReportEntity report);
}