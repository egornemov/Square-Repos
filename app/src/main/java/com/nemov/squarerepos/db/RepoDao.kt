package com.nemov.squarerepos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nemov.squarerepos.testing.OpenForTesting
import com.nemov.squarerepos.vo.Repo

/**
 * Interface for database access on Repo related operations.
 */
@Dao
@OpenForTesting
abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<Repo>)

    @Query(
        """
        SELECT * FROM Repo
        WHERE owner_login = :owner
        ORDER BY stars DESC"""
    )
    abstract fun loadRepositories(owner: String): LiveData<List<Repo>>
}
