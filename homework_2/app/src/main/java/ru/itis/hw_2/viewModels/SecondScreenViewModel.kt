package ru.itis.hw_2.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import ru.itis.hw_2.model.Post

class SecondScreenViewModel: ViewModel() {
    val posts = mutableStateListOf<Post>()
    fun addPost(title: String, content: String?) {
        posts.add(Post(title, content?:""))
    }
}