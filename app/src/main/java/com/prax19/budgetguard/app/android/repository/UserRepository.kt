package com.prax19.budgetguard.app.android.repository

import com.prax19.budgetguard.app.android.BuildConfig
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.dto.user.UserDTO
import com.prax19.budgetguard.app.android.data.model.User
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(
    private val api: BudgetGuardApi
) {

    private val API_KEY = BuildConfig.BGA_KEY

    suspend fun registerUser(user: User, password: String): Resource<String> {
        try {
            api.registerUser(API_KEY,
                UserDTO(
                    user.id,
                    user.firstName,
                    user.lastName,
                    user.email,
                    password,
                    user.role,
                    user.locked,
                    user.enabled

                )
            )
        } catch (e: Exception) {
            return Resource.Error("An error occurred during registration process.")
        }
        return Resource.Success("User registered successfully!")
    }

}