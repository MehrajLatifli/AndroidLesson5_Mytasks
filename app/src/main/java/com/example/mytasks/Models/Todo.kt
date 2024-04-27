// Todo.kt
package com.example.mytasks.Models

import java.io.Serializable

data class Todo(
    var title: String? = null,
    var text: String? = null,
): Serializable
