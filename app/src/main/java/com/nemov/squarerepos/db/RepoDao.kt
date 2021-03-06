package com.nemov.squarerepos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nemov.squarerepos.vo.Repo

/**
 * Interface for database access on Repo related operations.
 */
@Dao
abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<Repo>)

    @Query("SELECT * FROM Repo ORDER BY stars DESC")
    abstract fun loadRepositories(): LiveData<List<Repo>>
}
