package com.nemov.squarerepos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Repo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao
}
