package com.hasbite.app.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")

    data object Register : Routes("register")

    data object ForgotPassword : Routes("forgot_password")

    // 🔥 Home yerine artık ana sayfamız AI
    data object AI : Routes("ai_screen")
    data object Explore : Routes("explore")
    data object Favorites : Routes("favorites")
    data object Profile : Routes("profile")

    data object EditProfile : Routes("edit_profile")

    data object AccountSettings : Routes("account_settings")
    data object ShoppingList : Routes("shopping_list")

    data object MyCollections : Routes("my_collections")

    data object InviteFriends : Routes("invite_friends")
    data object PersonalInformation : Routes("personal_information")
    data object EmailAddress : Routes("email_address")
    data object ChangePassword : Routes("change_password")

    data object AIRecipe : Routes("ai_recipe")

    data object RecipeDetail :
        Routes("recipe_detail/{recipeId}/{isSavedRecipe}") {

        fun createRoute(
            recipeId: String,
            isSavedRecipe: Boolean
        ) = "recipe_detail/$recipeId/$isSavedRecipe"
    }

    data object CollectionDetail : Routes("collection_detail/{category}") {
        fun createRoute(category: String) = "collection_detail/$category"
    }
}