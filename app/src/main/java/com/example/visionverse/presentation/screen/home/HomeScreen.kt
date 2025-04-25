package com.example.visionverse.presentation.screen.home

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.visionverse.MainActivity
import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.presentation.navigation.Screens
import com.example.visionverse.presentation.navigation.Screens.MangaScreen

@Composable
fun HomeScreen(modifier: Modifier = Modifier, activity: MainActivity, context: Context, onNavigateToManga: (MangaEntity) -> Unit = {}) {
    var selectedItem by remember { mutableIntStateOf(0) }

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Manga",
            route = Screens.MangaScreen,
            selectedIcon = Icons.Default.Book,
            unselectedIcon = Icons.Outlined.Book,
            hasNews = false,
            badges = 0
        ),
        BottomNavItem(
            title = "Face Recognition",
            route = Screens.FaceRecognitionScreen,
            selectedIcon = Icons.Default.Face,
            unselectedIcon = Icons.Outlined.Face,
            hasNews = false,
            badges = 0
        ),
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Set the content based on the selected item
        when (selectedItem) {
            0 -> MangaScreen(modifier = Modifier.padding(innerPadding)) {
                onNavigateToManga(it)
            }
            1 -> FaceRecognitionScreen(modifier = Modifier.padding(innerPadding), context = context, activity = activity)
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: Screens,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badges: Int
)