package com.hasbite.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hasbite.app.navigation.Routes
import com.hasbite.app.ui.screens.PersonalInformationScreen
import com.hasbite.app.ui.screens.EmailAddressScreen
import com.hasbite.app.ui.screens.ChangePasswordScreen
import com.hasbite.app.ui.screens.InviteFriendsScreen
import com.hasbite.app.ui.screens.MyCollectionsScreen
import com.hasbite.app.ui.screens.ShoppingListScreen
import com.hasbite.app.ui.screens.AccountSettingsScreen
import com.hasbite.app.ui.screens.EditProfileScreen
import com.hasbite.app.ui.screens.SplashScreen
import com.hasbite.app.ui.screens.LoginScreen
import com.hasbite.app.ui.screens.RegisterScreen
import com.hasbite.app.ui.screens.ForgotPasswordScreen
import com.hasbite.app.ui.screens.MainScaffold
import com.hasbite.app.ui.screens.HomeScreen
import com.hasbite.app.ui.screens.ExploreScreen
import com.hasbite.app.ui.screens.FavoritesScreen
import com.hasbite.app.ui.screens.ProfileScreen
import com.hasbite.app.ui.screens.AIRecipeScreen
import com.hasbite.app.ui.screens.RecipeDetailScreen
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = modifier
    ) {

        composable(Routes.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // Şimdilik fake login: direkt Home
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Routes.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Routes.ForgotPassword.route)
                }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onCreateAccountClick = { username, email, password, confirmPassword ->
                    // Şimdilik backend yok, direkt tekrar login ekranına dön
                    navController.popBackStack()
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onResetClick = { email ->
                    // Şimdilik backend yok, reset'e basınca tekrar login ekranına dön
                    navController.popBackStack()
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.Home.route) {
            MainScaffold(navController) { m ->
                HomeScreen(
                    modifier = m,
                    onOpenAI = {
                        navController.navigate(Routes.AIRecipe.route)
                    },
                    onOpenRecipeDetail = { recipeId ->
                        navController.navigate(Routes.RecipeDetail.createRoute(recipeId))
                    }
                )
            }
        }

        composable(Routes.Explore.route) {
            MainScaffold(navController) { m ->
                ExploreScreen(
                    modifier = m,
                    onOpenRecipeDetail = { recipeId ->
                        navController.navigate(Routes.RecipeDetail.createRoute(recipeId))
                    }
                )
            }
        }

        composable(Routes.Favorites.route) {
            MainScaffold(navController) { m ->
                FavoritesScreen(
                    modifier = m,
                    onOpenRecipeDetail = { recipeId ->
                        navController.navigate(Routes.RecipeDetail.createRoute(recipeId))
                    }
                )
            }
        }

        composable(Routes.Profile.route) {
            MainScaffold(navController) { m ->
                ProfileScreen(
                    modifier = m,
                    onOpenEditProfile = {
                        navController.navigate(Routes.EditProfile.route)
                    },
                    onOpenAccountSettings = {
                        navController.navigate(Routes.AccountSettings.route)
                    },
                    onOpenCollections = {
                        navController.navigate(Routes.MyCollections.route)
                    },
                    onOpenShoppingList = {
                        navController.navigate(Routes.ShoppingList.route)
                    },
                    onOpenInviteFriends = {
                        navController.navigate(Routes.InviteFriends.route)
                    }
                )
            }
        }
        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.AccountSettings.route) {
            AccountSettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onOpenPersonalInformation = {
                    navController.navigate(Routes.PersonalInformation.route)
                },
                onOpenEmailAddress = {
                    navController.navigate(Routes.EmailAddress.route)
                },
                onOpenChangePassword = {
                    navController.navigate(Routes.ChangePassword.route)
                }
            )
        }

        composable(Routes.PersonalInformation.route) {
            PersonalInformationScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.EmailAddress.route) {
            EmailAddressScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ShoppingList.route) {
            ShoppingListScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MyCollections.route) {
            MyCollectionsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.InviteFriends.route) {
            InviteFriendsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.AIRecipe.route) {
            AIRecipeScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.RecipeDetail.route) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "default"

            RecipeDetailScreen(
                recipeId = recipeId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}