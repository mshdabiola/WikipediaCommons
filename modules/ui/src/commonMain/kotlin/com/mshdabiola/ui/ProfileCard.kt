package com.mshdabiola.ui

import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mshdabiola.designsystem.icon.WcsIcons

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    name: String = "Lawal abiola",
    email: String = "Mshd@gmail.com",
) {
    ListItem(
        modifier = modifier,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = { Text(name) },
        supportingContent = { Text(email) },
        leadingContent = { Icon(WcsIcons.Person, "person") },
        trailingContent = { Icon(WcsIcons.Logout, "out", tint = Color.Red) },
    )
}
