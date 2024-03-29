package com.prax19.budgetguard.app.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prax19.budgetguard.app.android.presentation.budget_details.BudgetDetailsScreen
import com.prax19.budgetguard.app.android.presentation.main_screen.MainScreen
import com.prax19.budgetguard.app.android.presentation.sign_in.SignInScreen
import com.prax19.budgetguard.app.android.presentation.sign_up.SignUpScreen
import com.prax19.budgetguard.app.android.ui.theme.BudgetGuardTheme
import com.prax19.budgetguard.app.android.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            BudgetGuardTheme {
                Surface {
                    val navConstoller = rememberNavController()
                    NavHost(
                        navController = navConstoller,
                        startDestination = Screen.SignInScreen.route
                    ) {
                        composable(route = Screen.MainScreen.route) {
                            MainScreen(navConstoller)
                        }
                        composable(
                            route = Screen.BudgetDetailsScreen.route +
                                    "?budgetId={budgetId}",
                            arguments = listOf(
                                navArgument(
                                    name = "budgetId"
                                ) {
                                    type = NavType.LongType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            BudgetDetailsScreen(navConstoller)
                        }
                        composable(route = Screen.SignInScreen.route) {
                            SignInScreen(navConstoller)
                        }
                        composable(route = Screen.EditUserDetailsScreen.route) {
                            SignUpScreen(navConstoller)
                        }
                    }
                }
            }
        }
    }
}