package com.prax19.budgetguard.app.android.repository

import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(
    private val api: BudgetGuardApi
) {



}