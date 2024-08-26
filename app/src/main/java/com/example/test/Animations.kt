package com.example.test

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun ChatScreen(){
    val list=UsersData()
    LazyColumn {
        items(list){
            val extend by remember {
                mutableStateOf(false)
            }
            Row(modifier= Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)){
                Image(painter = painterResource(id = R.drawable.img) ,
                    contentDescription =null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)  )
                Spacer(modifier = Modifier.width(10.dp))
                Column() {
                    Text(text = it.author)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = it.msg, maxLines = 1)
                }
            }
        }
    }

}
data class ListOfUsers(
    val author : String,
    val msg : String
)
fun UsersData() : List<ListOfUsers>{
    val list= listOf(
        ListOfUsers("Alice", "Hi there! Just checking in to see how you’re doing. Let me know if you’re free for a chat. Looking forward to catching up!"),
        ListOfUsers("Bob", "Hey Alice, I’m doing great, thanks for asking! How about we meet up this weekend? I’m free on Saturday afternoon."),
        ListOfUsers("Charlie", "Hello everyone, I’m organizing a get-together next Friday. It would be great to have all of you there. Let me know if you can make it!"),
        ListOfUsers("Diana", "Hi all, I just wanted to say thank you for the support. Your help means a lot to me. Can’t wait to catch up with you all soon."),
        ListOfUsers("Eve", "Hey team, We’ve got a project deadline coming up. Let’s finalize the details in our next meeting. Your input will be invaluable!"),
        ListOfUsers("Frank", "Greetings everyone, I’m excited about our upcoming trip. I’ve planned a few activities that I think you’ll enjoy. Looking forward to the adventure!"),
        ListOfUsers("Alice", "Hi there! Just checking in to see how you’re doing. Let me know if you’re free for a chat. Looking forward to catching up!"),
    ListOfUsers("Bob", "Hey Alice, I’m doing great, thanks for asking! How about we meet up this weekend? I’m free on Saturday afternoon."),
    ListOfUsers("Charlie", "Hello everyone, I’m organizing a get-together next Friday. It would be great to have all of you there. Let me know if you can make it!"),
    ListOfUsers("Diana", "Hi all, I just wanted to say thank you for the support. Your help means a lot to me. Can’t wait to catch up with you all soon."),
    ListOfUsers("Eve", "Hey team, We’ve got a project deadline coming up. Let’s finalize the details in our next meeting. Your input will be invaluable!"),
    ListOfUsers("Frank", "Greetings everyone, I’m excited about our upcoming trip. I’ve planned a few activities that I think you’ll enjoy. Looking forward to the adventure!")
    )
    return list
}
