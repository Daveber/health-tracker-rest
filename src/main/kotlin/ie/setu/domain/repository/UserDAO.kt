package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

/**
 * Manages database transactions and returns the results
 */

class UserDAO {

    /**
     * Gets all users
     * @return list of users
     */
    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    /**
     * retrieves user by specified [id]
     * @return user where id matches
     */
    fun findUserById(id: Int): User? {
        return transaction {
            Users.selectAll().where { Users.id eq id}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    /**
     * retrieves a user by specified [email]
     * @return user where email matches
     */
    fun findUserByEmail(email: String): User? {
        return transaction {
            Users.selectAll().where {Users.email eq email}
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }

    /**
     * adds a [user] to the users table
     * @return the id of the added user
     */
    fun save(user: User) : Int? {
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            } get Users.id
        }
    }

    /**
     * Updates the [user] with the specific [id]
     */
    fun update(id: Int, user: User) : Int {
        return transaction {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }

    /**
     * Deletes user with specified [id]
     */
    fun delete(id: Int?) : Int? {
        return transaction {
            Users.deleteWhere { Users.id eq id as Int }
        }
    }
}