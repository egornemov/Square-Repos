package com.nemov.squarerepos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nemov.squarerepos.vo.Repo

/**
 * Main database description.
 */
@Database(
    entities = [Repo::class],
    version = 1,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}
