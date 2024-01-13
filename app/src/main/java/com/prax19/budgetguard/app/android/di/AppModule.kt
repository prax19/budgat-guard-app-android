package com.prax19.budgetguard.app.android.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.api.BudgetGuardInterceptor
import com.prax19.budgetguard.app.android.repository.AuthRepository
import com.prax19.budgetguard.app.android.repository.BudgetRepository
import com.prax19.budgetguard.app.android.repository.OperationRepository
import com.prax19.budgetguard.app.android.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesAuthRepository(
        api: BudgetGuardApi,
        preferences: SharedPreferences
    ) = AuthRepository(api, preferences)

    @Provides
    @Singleton
    fun provideUserRepository(
        api: BudgetGuardApi
    ) = UserRepository(api)

    @Provides
    @Singleton
    fun provideBudgetRepository(
        api: BudgetGuardApi,
        authRepository: AuthRepository,
        operationRepository: OperationRepository
    ) = BudgetRepository(api, authRepository, operationRepository)

    @Provides
    @Singleton
    fun provideOperationRepository(
        api: BudgetGuardApi,
        authRepository: AuthRepository,
    ) = OperationRepository(api, authRepository)

    @Provides
    @Singleton
    fun providesSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideBudgetGuardApi(interceptor: BudgetGuardInterceptor): BudgetGuardApi {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BudgetGuardApi.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            )
            .build()
            .create(BudgetGuardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBudgetGuardInterceptor(
        preferences: SharedPreferences
    ): BudgetGuardInterceptor {
        return BudgetGuardInterceptor(preferences)
    }

}