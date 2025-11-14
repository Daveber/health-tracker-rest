package ie.setu.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory

val user1 = users[0]
val user2 = users[1]
val user3 = users[2]

class UserDaoTest {

    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        return userDAO
    }

    companion object {

        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
                assertEquals(user1, userDAO.findUserById(user1.id))
                assertEquals(user2, userDAO.findUserById(user2.id))
                assertEquals(user3, userDAO.findUserById(user3.id))
            }
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(null, userDAO.findUserById(4))
            }
        }

        @Test
        fun `find user by id that exists, results in a correct user returned`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(user3, userDAO.findUserById(user3.id))
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {

                SchemaUtils.create(Users)
                val userDAO = UserDAO()

                assertEquals(0, userDAO.getAll().size)
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(null,userDAO.findUserByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by existing email, results in correct user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(user1,userDAO.findUserByEmail(user1.email))
            }
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun `updating existing user in table results in successfull update`() {

            transaction {

                val userDAO = populateUserTable()

                val user3Updated = User(3, "new username", "new@email.ie")
                userDAO.update(user3.id, user3Updated)
                assertEquals(user3Updated, userDAO.findUserById(user3.id))
            }
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {

                val userDAO = populateUserTable()

                val user4Updated = User(4, "new username", "new email")
                userDAO.update(4, user4Updated)
                assertEquals(null, userDAO.findUserById(4))
                assertEquals(3,userDAO.getAll().size)

            }
        }
    }

    @Nested
    inner class Deleteusers {
        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(4)
                assertEquals(3, userDAO.getAll().size)
            }
        }
    }
}