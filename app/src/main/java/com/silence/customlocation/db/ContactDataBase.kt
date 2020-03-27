package com.silence.customlocation.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.silence.customlocation.db.MigrationClass.MIGRATION_1_2
import com.silence.customlocation.db.MigrationClass.MIGRATION_2_3
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Contact::class,User::class], version = 1, exportSchema = false)
abstract class ContactDataBase : RoomDatabase() {


    abstract fun contactDao(): ContactDao


    companion object {
        @Volatile
        var INSTANCE: ContactDataBase? = null

        fun getDataBase(context: Context, scope: CoroutineScope): ContactDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDataBase::class.java,
                    "contact_database"
                ).addCallback(ContactDatabaseCallback(context, scope))
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }



}