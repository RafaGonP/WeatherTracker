package com.rafagonp.weathertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rafagonp.weathertracker.domain.model.DayBO
import com.rafagonp.weathertracker.ui.LIST_SCREEN_TAG
import com.rafagonp.weathertracker.ui.SCAFFOLD_TAG
import com.rafagonp.weathertracker.ui.screen.DayDetailScreen
import com.rafagonp.weathertracker.ui.screen.DaysListScreen
import com.rafagonp.weathertracker.ui.theme.WeatherTrackerTheme
import com.rafagonp.weathertracker.viewmodels.DaysListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTrackerTheme {
                val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<DayBO>()
                val scope = rememberCoroutineScope()

                NavigableListDetailPaneScaffold(
                    navigator = scaffoldNavigator,
                    listPane = {
                        AnimatedPane {
                            ListPane(scope, scaffoldNavigator)
                        }
                    },
                    detailPane = {
                        AnimatedPane {
                            DetailPane(scope, scaffoldNavigator)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .testTag(SCAFFOLD_TAG)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListPane(scope: CoroutineScope, scaffoldNavigator: ThreePaneScaffoldNavigator<DayBO>){
    DaysListScreen(
        hiltViewModel<DaysListViewModel>(),
        onItemClick = { day ->
            navigateToDetail(scope, scaffoldNavigator, day)
        },
        modifier = Modifier.testTag(LIST_SCREEN_TAG)
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun navigateToDetail(scope: CoroutineScope, scaffoldNavigator: ThreePaneScaffoldNavigator<DayBO>, day: DayBO){
    scope.launch {
        scaffoldNavigator.navigateTo(
            ListDetailPaneScaffoldRole.Detail,
            day
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DetailPane(scope: CoroutineScope, scaffoldNavigator: ThreePaneScaffoldNavigator<DayBO>){
    scaffoldNavigator.currentDestination?.contentKey?.let {
        Column {
            if (scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = {
                        scope.launch {
                            scaffoldNavigator.navigateBack(
                                BackNavigationBehavior.PopUntilScaffoldValueChange
                            )
                        }
                    }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            DayDetailScreen(
                it,
                modifier = Modifier.testTag(LIST_SCREEN_TAG)
            )
        }
    }
}


/*
@Composable
fun NavigationComponent(
    navController: NavHostController,
    navigator: Navigator
) {
    LaunchedEffect("navigation") {
        navigator.sharedFlow.onEach {
            navController.navigate(it.label)
        }.launchIn(this)
    }

    NavHost(
        navController = navController,
        startDestination = Navigator.NavTarget.DaysList.label
    ) {
        composable(Navigator.NavTarget.DaysList.label) { backStackEntry ->
            DaysListScreen(
                hiltViewModel<DaysListViewModel>(backStackEntry),
                onNavigateToDayDetail = {
                    navigator.navigateTo(Navigator.NavTarget.DayDetail)
                }
            )
        }
        composable(Navigator.NavTarget.DayDetail.label) { backStackEntry ->
            DayDetailScreen(
                hiltViewModel<DayDetailViewModel>(backStackEntry),
                onNavigateBackToList = {
                    navigator.navigateTo(Navigator.NavTarget.DaysList)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherTrackerTheme {
        NavigationComponent(rememberNavController(), navigator = Navigator())
    }
}
*/
