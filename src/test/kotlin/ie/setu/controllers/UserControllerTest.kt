package ie.setu.controllers

import ie.setu.config.DBConfig
//import ie.setu.controllers.UserController.updateUser
import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.TestDatabaseConfig
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.nonexisitingid
import ie.setu.helpers.users
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import ie.setu.repository.user1
import ie.setu.repository.user2
import ie.setu.repository.user3
import ie.setu.utils.jsonToObject
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import kong.unirest.core.Unirest
import org.eclipse.jgit.util.HttpSupport.response
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    //copied from userDAOTest
    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        return userDAO
    }

    /** Helper function to add user to test database **/
    private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }


    /** Helper function to get user by email from test database **/
    private fun retrieveUserByEmail(email: String): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    /** Helper function to update user in test database **/
    private fun retrieveUserById (id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    /** Helper function to delete user from test database **/
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id").asString()
    }

    /** Helper function to add a test user to database **/
    private fun updateUser (id: Int, name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

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
    inner class CreateUser {

        @Test
        fun `add a user with correct details - returns 201 response` () {

            //Add user and verify return code
            val addResponse = addUser(validName, validEmail)
            assertEquals(201, addResponse.status)

            //Retrieving added user and verifying return code
            val retrievedResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrievedResponse.status)

            //Verifying User body contents
            val retrievedUser : User = jsonToObject(addResponse.body.toString())
            assertEquals(validName, retrievedUser.name)
            assertEquals(validEmail, retrievedUser.email)

            //Restoring db to previous state by deleting user and verifying return code
            val deleteResponse = deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }

        @Test
        fun `adding multiple users and checking if they can be retrieved` () {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
                assertEquals(users[0].name, userDAO.findUserByEmail(users[0].email)?.name)
                assertEquals(users[1].name, userDAO.findUserByEmail(users[1].email)?.name)
                assertEquals(users[2].name, userDAO.findUserByEmail(users[2].email)?.name)
            }
        }
    }

    @Nested
    inner class ReadUser {

        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            if (response.status == 200) {
                val retrievedUsers: ArrayList<User> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedUsers.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user by id when user does not exists - returns 404`() {
            val id = Integer.MIN_VALUE
            val retrieveResponse = Unirest.get(origin + "/api/users/${id}").asString()
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by id when id exists - returns 200`() {

            //Add the user
            val addResponse = addUser(validName, validEmail)
            val addedUser : User = jsonToObject(addResponse.body.toString())

            //Retrieve the added user from database and verify return code
            val retrieveResponse = retrieveUserById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //Delete added user from database
            deleteUser(addedUser.id)
        }

        @Test
        fun `get user by email when email exists - returns 200`() {

            //Add user
            addUser(validName, validEmail)

            //Retrieve added user from database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Restore db by deleting added user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            deleteUser(retrievedUser.id)
        }

        @Test
        fun `get user by email when user does not exist - returns 404 response`() {
            val retrieveResponse = Unirest.get(origin + "/api/users/email/${nonExistingEmail}").asString()
            assertEquals(404, retrieveResponse.status)
        }
    }

    @Nested
    inner class UpdateUser {

        @Test
        fun `updating a user when it exists - returns 204`() {

            //Add user to perform update on
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"
            val addedResponse = addUser(validName, validEmail)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Update the email and name of user
            assertEquals(204, updateUser(addedUser.id, updatedName, updatedEmail).status)

            //Retrieve updated user and assert details are correct
            val updatedUserResponse = retrieveUserById(addedUser.id)
            val updatedUser : User = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(updatedName, updatedUser.name)
            assertEquals(updatedEmail, updatedUser.email)

            //Restoring db by deleting user
            deleteUser(addedUser.id)
            }

        @Test
        fun `updating a user when it doesn't exist - response 404`() {

            //text data
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"

            //updating
            assertEquals(404, updateUser(-1, updatedName, updatedEmail).status)
        }
    }

    @Nested
    inner class DeleteUser {

        @Test
        fun `delete user by id when id exists`() {
            val addResponse = addUser(validName, validEmail)
            val addedUser: User = jsonToObject(addResponse.body.toString())

            val getResponse = retrieveUserById(addedUser.id)
            assertEquals(200, getResponse.status)

            val delResponse = deleteUser(addedUser.id)
            assertEquals(204, delResponse.status)

            val getResponseAfterDelete = retrieveUserById(addedUser.id)
            assertEquals(404, getResponseAfterDelete.status)
        }

        @Test
        fun `delete user by non-existing id`() {
            val delResponse = deleteUser(nonexisitingid)
            assertEquals(404, delResponse.status)
        }
    }
}