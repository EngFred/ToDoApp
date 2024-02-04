package com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
    // Create the new table
    db.execSQL(
        "CREATE TABLE deleted_todos (" +
                "id INTEGER PRIMARY KEY NOT NULL," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "timeStamp INTEGER NOT NULL," +
                "completed INTEGER NOT NULL," +
                "archived INTEGER NOT NULL," +
                "isSynced INTEGER NOT NULL)"
    )
    }
//    database.execSQL("CREATE TABLE deleted_todos (id INTEGER PRIMARY KEY, title TEXT, description TEXT, timeStamp INTEGER, completed INTEGER, archived INTEGER, isSynced INTEGER)")
}


val MIGRATION_2_1 = object : Migration(2, 1) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `todo` ADD COLUMN `dueDate` INTEGER NULL");
        db.execSQL("ALTER TABLE `deleted_todos` ADD COLUMN `dueDate` INTEGER NULL");
        db.execSQL("ALTER TABLE `saved_to_post_todos` ADD COLUMN `dueDate` INTEGER NULL");
    }
}