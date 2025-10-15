package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ie.setu.utils.mapToUser

class UserDAO {


    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    fun findUserById(id: Int): User? {
        return null;
    }

    fun findUserByEmail(email: String): User? {
        return null;
    }

    fun save(user: User) {

    }

    fun update(id: Int, user: User) {

    }

    fun delete(id: Int?) {

    }
}