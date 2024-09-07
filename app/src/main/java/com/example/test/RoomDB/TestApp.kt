package com.example.test.RoomDB

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update


@Database(entities = [Item :: class], version = 1)
abstract class InventoryDatabase : RoomDatabase(){
    abstract fun itemDao() : ItemDao

    companion object{

        @Volatile
        private var INSTANCE : InventoryDatabase?=null

        fun getDatabase(context: Context) : InventoryDatabase{
            synchronized(this){
                if(INSTANCE==null){
                    var instance = Room.databaseBuilder(context,InventoryDatabase::class.java,"InvertoryDatabase").build()
                    INSTANCE = instance
                    return instance
                }
                return INSTANCE!!
            }
        }
    }
}
@Entity(tableName = "ItemsTable")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id : Int,

    val name : String,
    val price : Int,
    val quantity : Int
)
class ItemRepositery(private val dao: ItemDao){
    suspend fun insertItem(item: Item){
        dao.insertItem(item)
    }

    suspend fun getAllItems() : List<Item> {
        return dao.getAllitems()
    }
}

@Dao
interface ItemDao{

    @Insert
    suspend fun insertItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("Select * from itemstable")
    suspend fun getAllitems() : List<Item>
}

class ItemViewModel(private val repositery: ItemRepositery) : ViewModel(){

    suspend fun getAllitems() : List<Item>{
        return repositery.getAllItems()
    }
    suspend fun insertItem(item: Item){
        repositery.insertItem(item)
    }
}

class ViewModelFactory(private val repositery: ItemRepositery) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repositery) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}