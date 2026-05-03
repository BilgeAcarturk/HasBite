package com.hasbite.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hasbite.app.ui.screens.*

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

        // --- 1. SPLASH & AUTH ---
        composable(Routes.Splash.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navController.navigate(Routes.AI.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(Routes.Register.route) },
                onForgotPasswordClick = { navController.navigate(Routes.ForgotPassword.route) }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onCreateAccountClick = { _, _, _, _ -> navController.popBackStack() },
                onBackToLoginClick = { navController.popBackStack() }
            )
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(onBackToLoginClick = { navController.popBackStack() })
        }

        // --- 2. MAIN TABS (SCAFFOLD) ---

        // 🔥 ANA SAYFA: AI ASİSTAN (Artık Home yok, bu var)
        composable(Routes.AI.route) {
            MainScaffold(navController) { m ->
                AIRecipeScreen(
                    modifier = m,
                    onBackClick = null, // Ana sayfa olduğu için geri butonu gizli
                    query = ""
                )
            }
        }

        composable(Routes.Explore.route) {
            MainScaffold(navController) { m ->
                ExploreScreen(
                    modifier = m,
                    onOpenRecipeDetail = { route ->
                        if (route.startsWith("ai_generate")) navController.navigate(route)
                        else navController.navigate("recipe_detail/$route")
                    }
                )
            }
        }

        composable(Routes.Favorites.route) {
            MainScaffold(navController) { m ->
                FavoritesScreen(
                    modifier = m,
                    onOpenRecipeDetail = { id -> navController.navigate("recipe_detail/$id") }
                )
            }
        }

        composable(Routes.Profile.route) {
            MainScaffold(navController) { m ->
                ProfileScreen(
                    modifier = m,
                    onOpenEditProfile = { navController.navigate(Routes.EditProfile.route) },
                    onOpenAccountSettings = { navController.navigate(Routes.AccountSettings.route) },
                    onOpenCollections = { navController.navigate(Routes.MyCollections.route) },
                    onOpenShoppingList = { navController.navigate(Routes.ShoppingList.route) },
                    onOpenInviteFriends = { navController.navigate(Routes.InviteFriends.route) }
                )
            }
        }

        // --- 3. SPECIAL AI & DETAILS ---

        // Explore'dan tetiklenen sorgulu AI ekranı
        composable("ai_generate/{query}") { backStackEntry ->
            val rawQuery = backStackEntry.arguments?.getString("query") ?: ""
            val decodedQuery = java.net.URLDecoder.decode(rawQuery, "UTF-8")

            AIRecipeScreen(
                onBackClick = { navController.popBackStack() },
                query = decodedQuery
            )
        }

        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "default"
            RecipeDetailScreen(
                recipeId = recipeId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- 4. SETTINGS & PROFILE DETAILS ---

        composable(Routes.AccountSettings.route) {
            AccountSettingsScreen(
                onBackClick = { navController.popBackStack() },
                onOpenChangePassword = { navController.navigate(Routes.ChangePassword.route) }
            )
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.ChangePassword.route) {
            ChangePasswordScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.PersonalInformation.route) {
            PersonalInformationScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.EmailAddress.route) {
            EmailAddressScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.MyCollections.route) {
            MyCollectionsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.ShoppingList.route) {
            ShoppingListScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.InviteFriends.route) {
            InviteFriendsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}