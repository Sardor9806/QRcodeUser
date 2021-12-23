package com.example.user.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.user.entity.Roomentity


@Database(entities = [Roomentity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): Userdao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null

        fun getDatabase(context: Context):UserDatabase
        {
            val tempInstance= instance
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this){
                val instancee= Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "sardor"
                ).build()
                instance=instancee
                return instancee
            }
        }

    }
}
