package com.example.test

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.coroutineContext

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao

    companion object{

        @Volatile
        private var INSTANCE : UserDatabase?=null

        fun getDatabase(context: Context) : UserDatabase{
            return  INSTANCE?: synchronized(this){

                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "UserTable"
                ).build()
                INSTANCE=instance
                instance
            }
        }
    }
}

class UserViewModel(private val repositery: Repositery) : ViewModel(){
    suspend fun insertUser(user : User){
        repositery.insertUser(user)
    }
    suspend fun getAllUser() : List<User>{
        return repositery.getAllUser()
    }
    suspend fun DeleteUsers(){
        repositery.DeleteUsers()
    }
}
class Repositery(private val userDao: UserDao){
    suspend fun insertUser(user : User){
        userDao.insertUser(user)
    }
    suspend fun getAllUser() : List<User>{
        return userDao.allUsers()
    }
    suspend fun DeleteUsers(){
        userDao.deleteAll()
    }
}
class ViewModelFactory(private val repositery: Repositery) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repositery) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
fun Initializing(){
    val userDao= UserDatabase.getDatabase(LocalContext.current).userDao()

    val repositery=Repositery(userDao)

    val viewModel = ViewModelFactory(repositery)

    val UserViewModel : UserViewModel=viewModel(factory = viewModel)

    mainScreen(UserViewModel)
}
@Composable
fun mainScreen(userViewModel: UserViewModel){

    //val coroutineScope = rememberCoroutineScope()

    var usersAll by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    LaunchedEffect(Unit) {
        usersAll=userViewModel.getAllUser()
    }
    UIScreen(Userlist =usersAll ,username = {
        CoroutineScope(Dispatchers.Main).launch {
            userViewModel.insertUser(User(name = it))
            usersAll=userViewModel.getAllUser()
        } },
        deleteUser = {CoroutineScope(Dispatchers.Main).launch {
            userViewModel.DeleteUsers()
            usersAll=userViewModel.getAllUser()
        }},
        )
}

@Composable
fun UIScreen(Userlist : List<User>,username:(String)->Unit,deleteUser:()->Unit){
    var UserName by remember {
        mutableStateOf("")
    }
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center){
        LazyColumn {
            items(Userlist){
                Text(text = it.name.toString(), color = Color.Red)
            }
        }
        TextField(value = UserName, onValueChange = {UserName=it})
        Button(onClick = {
            if (UserName.isNotBlank()){
                username(UserName)
                UserName=""
            }
        }
        ) {
            Text(text = "Submit")
        }
        Button(onClick = { deleteUser() }) {
            Text(text = "DeleteAll")
        }
    }
}
@Entity(tableName = "UserTable")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id : Int=0,

    @ColumnInfo(name = "Name")
    val name : String?,
)
@Dao
interface UserDao {

    @Query("SELECT * FROM usertable")
    suspend fun allUsers() : List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM usertable")
    suspend fun deleteAll()

}
