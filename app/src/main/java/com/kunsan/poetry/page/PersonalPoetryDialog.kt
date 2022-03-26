package com.kunsan.poetry.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PersonalPoetryDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    var text by remember {
        mutableStateOf("")
    }

    if (show) {
        AlertDialog(onDismissRequest = onDismiss,
            title = {
                Text(text = "开始你的创作")
            },
            text = {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    OutlinedTextField(value = text, label = { Text("前半句") }, onValueChange = {
                        text = it
                    })
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = text, label = { Text("后半句") }, onValueChange = {
                        text = it
                    })
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = text, label = { Text("题名") }, onValueChange = {
                        text = it
                    })
                }

            },
            buttons = {
                Button(onClick = onConfirm) {
                    Text("完成")
                }
            }
        )
    }

}