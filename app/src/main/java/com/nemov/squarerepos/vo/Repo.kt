package com.nemov.squarerepos.vo

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Using name/owner_login as primary key instead of id since name/owner_login is always available
 * vs id is not.
 */
@Entity(primaryKeys = ["name"])
data class Repo(
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("stargazers_count")
    val stars: Int
)
