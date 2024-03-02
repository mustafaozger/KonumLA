package com.example.mevltbul.Hilt

import com.example.mevltbul.Repository.DetailPageDaoRepo
import com.example.mevltbul.Repository.MessagePageDaoRepository
import com.example.mevltbul.Repository.UserDaoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDetailPageDaoRepo():DetailPageDaoRepo{
        return DetailPageDaoRepo()
    }

    @Provides
    @Singleton
    fun provideUserDaoRepo(): UserDaoRepository {
        return UserDaoRepository()
    }
    @Provides
    @Singleton
    fun provideMessagePageDaoRepo(): MessagePageDaoRepository {
        return MessagePageDaoRepository()
    }

}