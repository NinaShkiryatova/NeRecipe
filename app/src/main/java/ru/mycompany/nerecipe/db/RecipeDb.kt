package ru.mycompany.nerecipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class, StepEntity::class], version = 2, exportSchema = false)
abstract class RecipeDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var instance: RecipeDb? = null

        fun getInstance(context: Context): RecipeDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, RecipeDb::class.java, "rec.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}
