package com.ms.app

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface DaggerActivityModule {

    companion object {

        @Provides
        fun provideAppCompatActivity(activity: Activity): AppCompatActivity {
            return activity as AppCompatActivity
        }

    }

}