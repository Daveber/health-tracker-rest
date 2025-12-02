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
import ie.setu.helpers.TestDatabaseConfig
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.nonexisitingid
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
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

    internal fun cleanUserTable() {
        val userDAO = UserDAO()
        userDAO.delete(user1.id)
        userDAO.delete(user2.id)
        userDAO.delete(user3.id)
    }

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupInMemoryDatabase() {
            TestDatabaseConfig.connect()
        }
    }

    @BeforeEach
    fun resetDatabase() {
        TestDatabaseConfig.reset()
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

                cleanUserTable()
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

                cleanUserTable()
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(null, userDAO.findUserById(4))

                cleanUserTable()
            }
        }

        @Test
        fun `find users by id that exists, results in a correct users returned`() {
            transaction {
                val userDAO = populateUserTable()

                //assertEquals(user1, userDAO.findUserById(user1.id))
                assertEquals(arrayListOf<User>(user1, user2, user3), userDAO.getAll())

                cleanUserTable()
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {

                val userDAO = UserDAO()

                assertEquals(0, userDAO.getAll().size)

                cleanUserTable()
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(null,userDAO.findUserByEmail(nonExistingEmail))

                cleanUserTable()
            }
        }

        @Test
        fun `get user by existing email, results in correct user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(user1,userDAO.findUserByEmail(user1.email))

                cleanUserTable()
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

                cleanUserTable()
            }
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {

                val userDAO = populateUserTable()

                val user4Updated = User(nonexisitingid, "new username", "new email")
                userDAO.update(nonexisitingid, user4Updated)
                assertEquals(null, userDAO.findUserById(4))
                assertEquals(3,userDAO.getAll().size)

                cleanUserTable()
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
                userDAO.delete(nonexisitingid)
                assertEquals(3, userDAO.getAll().size)

                cleanUserTable()
            }
        }
    }
}