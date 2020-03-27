package com.silence.customlocation.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


/**
 * 更新数据库
 */
object MigrationClass {

    /**
     * 数据库版本 1->2 新增user表
     */
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `user` (`uid` INTEGER PRIMARY KEY autoincrement, 'userName' Char(30) , 'userPhone' Char(30), 'userEmail' Char(30),'userPass' Char(30),'userIconPath' Char(60),'appKey' Char(30) )"
            )
        }
    }
    /**
     * 数据库版本 1->2 新增user表
     */
    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE User "
                        + " ADD COLUMN userNickName Char(30)"
            );
        }
    }

}