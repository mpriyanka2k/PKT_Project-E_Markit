package com.pkt.emarkit.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pkt.emarkit.db.data_models.Delivery

@Dao
public interface devilery_dao {

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun insertAll(vararg data: Delivery)

 @Query("DELETE FROM delivery")
 fun deleteAll()

 @Query("SELECT * FROM delivery WHERE order_code=:order_code")
 fun getDelivery(order_code:String):LiveData<Delivery>
}