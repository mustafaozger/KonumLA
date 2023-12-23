package com.example.mevltbul.Hilt

import com.example.mevltbul.Repository.DetailPageDaoRepo
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

}