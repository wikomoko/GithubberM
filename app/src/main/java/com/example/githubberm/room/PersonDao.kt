package com.example.githubberm.room

import androidx.room.*

@Dao
interface PersonDao {
    @Insert
    fun addPerson(person: Person)

    @Update
    fun updatePerson(person: Person)

    @Delete
    fun deletePerson(person: Person)

    @Query("select * from person")
    fun getPersons():List<Person>

    @Query("delete from person where userLogin=:person_id")
    fun removePerson(person_id: String)
}