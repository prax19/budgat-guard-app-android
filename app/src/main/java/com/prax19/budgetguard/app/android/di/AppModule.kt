package com.prax19.budgetguard.app.android.di

import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.repository.BudgetRepository
import com.prax19.budgetguard.app.android.repository.OperationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBudgetRepository(
        api: BudgetGuardApi
    ) = BudgetRepository(api)

    @Provides
    @Singleton
    fun provideOperationRepository(
        api: BudgetGuardApi
    ) = OperationRepository(api)

    @Provides
    @Singleton
    fun provideBudgetGuardApi(): BudgetGuardApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BudgetGuardApi.BASE_URL)
            .build()
            .create(BudgetGuardApi::class.java)
    }

}