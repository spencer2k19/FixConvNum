package com.yanncer.fixconvnum.di

import android.content.Context
import com.yanncer.fixconvnum.data.ContactsRepositoryImpl
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import com.yanncer.fixconvnum.domain.use_case.ContactUseCases
import com.yanncer.fixconvnum.domain.use_case.FixContacts
import com.yanncer.fixconvnum.domain.use_case.GetContacts
import com.yanncer.fixconvnum.domain.use_case.RemoveContact
import com.yanncer.fixconvnum.domain.use_case.UpdateContacts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContactsRepository(@ApplicationContext appContext: Context):ContactsRepository {
        return ContactsRepositoryImpl(appContext)
    }


    @Provides
    @Singleton
    fun provideContactUseCases(repository: ContactsRepository): ContactUseCases {
        return ContactUseCases(
            getContacts = GetContacts(repository),
            fixContacts = FixContacts(repository),
            removeContacts = RemoveContact(repository),
            updateContacts = UpdateContacts(repository)
        )
    }
}