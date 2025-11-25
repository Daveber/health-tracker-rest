package ie.setu.controllers

import ie.setu.config.DBConfig
import ie.setu.controllers.UserController.updateUser
import ie.setu.domain.Favourite
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Favourites
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FavouriteDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.favvalidactivityid1
import ie.setu.helpers.favvalidactivityid2
import ie.setu.helpers.favvaliduserid1
import ie.setu.helpers.favvaliduserid2
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.users
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import ie.setu.repository.activity1
import ie.setu.repository.activity2
import ie.setu.repository.samplefavourite1
import ie.setu.repository.samplefavourite2
import ie.setu.repository.samplefavourite3
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
import org.junit.jupiter.api.Nested
import ie.setu.controllers.UserControllerTest
import ie.setu.controllers.ActivityControllerTest
import ie.setu.helpers.TestDatabaseConfig
import ie.setu.helpers.nonexisitingid
import org.junit.jupiter.api.BeforeEach

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FavouriteControllerTest {

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)

        val userDAO = UserDAO()

        userDAO.save(user1) //id 1
        userDAO.save(user2) //id 2

        return userDAO
    }

    internal fun populateActivityTable(): ActivityDAO {
        SchemaUtils.create(Activities)

        val activityDAO = ActivityDAO()

        activityDAO.save(activity1) //activity id 1, user id 1
        activityDAO.save(activity2) //activity id 2, user id 2

        return activityDAO
    }

    internal fun populateFavouriteTable(): FavouriteDAO {

        SchemaUtils.create(Favourites)

        val favouriteDAO = FavouriteDAO()

        favouriteDAO.save(samplefavourite1) //id 1, user id 1, activity id 1
        favouriteDAO.save(samplefavourite2) //id 2, user id 2, activity id 2

        return favouriteDAO
    }

    /** Helper function to add favourite to test database **/
    private fun addFavourite(userId: Int, activityId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/favourites")
            .body("""
                 {
                    "userid": "$userId",
                    "activityid": "$activityId"
                 }
            """.trimIndent())
            .asJson()
    }

    /** Helper function to get all favourites **/
    private fun retrieveFavourites(): HttpResponse<String> {
        return Unirest.get(origin + "/api/favourites").asString()
    }

    /** Helper function to retrieve Favourite by favourite id **/
    private fun retrieveFavourite(favouriteId: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/favourites/${favouriteId}").asString()
    }

    /** Helper function to retrieve all favourites with specified user id **/
    private fun retrieveFavouritesByUserId(userId: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${userId}/favourites").asString()
    }

    /** Helper function to retrieve all favourites by activity id **/
    private fun retrieveFavouritesByActivityId(activityId: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/activities/${activityId}/favourites").asString()
    }

    /** Helper function to delete Favourite by id **/
    private fun deleteFavourite(favouriteId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/favourites/${favouriteId}").asString()
    }

    /** Helper function to delete favourites by user id**/
    private fun deleteFavouritesByUserId(userId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/${userId}/favourites").asString()
    }

    /** Helper function to delete favourites by activity id **/
    private fun deleteFavouritesByActivityId(activityId: Int): HttpResponse<String> {              
        return Unirest.delete(origin + "/api/activities/${activityId}/favourites").asString()     
    }

    /** Helper function to clear database **/
    private fun clearDB(){
        deleteFavouritesByUserId(favvaliduserid1)
        deleteFavouritesByUserId(favvaliduserid2)
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
    inner class CreateFavourite {

        @Test
        fun `add favourite with correct details`() {
            transaction {
                val userDao = populateUserTable()
                val activityDAO = populateActivityTable()
                val favouriteDAO = populateFavouriteTable()

                val addResponse = addFavourite(1, 1)
                assertEquals(201, addResponse.status)
            }
            clearDB()
        }

//        @Test
//        fun `add favourite with incorrect details`() {
//            transaction {
//                val userDao = populateUserTable()
//                val activityDAO = populateActivityTable()
//                val favouriteDAO = populateFavouriteTable()
//
//                val addResponse = addFavourite(-1, -1)
//                assertEquals(400, addResponse.status)
//            }
//        }
    }

    @Nested
    inner class ReadFavourite {

        @Test
        fun `get all favourites when favourite exists`() {
            addFavourite(favvaliduserid1,favvalidactivityid1)

            val getResponse = retrieveFavourites()
            assertEquals(200, getResponse.status)

            clearDB()
        }

        @Test
        fun `get all favourites empty`() {
            val getResponse = retrieveFavourites()
            assertEquals(404, getResponse.status)
        }

        @Test
        fun `get favourite by correct id`() {
           val addResponse = addFavourite(favvaliduserid1, favvalidactivityid1)
            val addedFavourite : Favourite = jsonToObject(addResponse.body.toString())

            val getResponse = retrieveFavourite(addedFavourite.id)

            assertEquals(200, getResponse.status)

            clearDB()
        }

        @Test
        fun `get favourite by incorrect id`() {
            val getResponse = retrieveFavourite(nonexisitingid)

            assertEquals(404, getResponse.status)
        }

        @Test
        fun `get favourites by existing userId`() {
            val addResponse = addFavourite(favvaliduserid1, favvalidactivityid1)
            val addedFavourite : Favourite = jsonToObject(addResponse.body.toString())

            val getResponse = retrieveFavourite(addedFavourite.id)
            assertEquals(200, getResponse.status)

            deleteFavouritesByUserId(favvaliduserid1)
            deleteFavouritesByUserId(favvaliduserid2)
        }

        @Test
        fun `get favourites by non-existing userId`() {
            val getResponse = retrieveFavourite(nonexisitingid)
            assertEquals(404, getResponse.status)
        }

        @Test
        fun `get favourites by existing activityId`() {
            val addResponse = addFavourite(favvaliduserid1, favvalidactivityid1)
            val addedFavourite : Favourite = jsonToObject(addResponse.body.toString())

            val getResponse = retrieveFavouritesByActivityId(addedFavourite.id)

            clearDB()
        }

        @Test
        fun `get favourites by non-existing activityId`() {
            val getResponse = retrieveFavouritesByActivityId(nonexisitingid)
            assertEquals(404, getResponse.status)
        }

    }

    @Nested
    inner class DeleteFavourite {

        @Test
        fun `delete favourite by existing id`() {
            val addResponse = addFavourite(favvaliduserid1, favvalidactivityid1)
            val addedFavourite : Favourite = jsonToObject(addResponse.body.toString())
            val delResponse = deleteFavourite(addedFavourite.id)

            assertEquals(204, delResponse.status)

            clearDB()

        }

        @Test
        fun `delete favourite by non-existing id`() {
            val delResponse = deleteFavourite(nonexisitingid)
            assertEquals(404, delResponse.status)
        }

        @Test
        fun `delete favourites by existing user id`() {
            val addResponse = addFavourite(favvaliduserid1, favvalidactivityid1)
            val addedFavourite : Favourite = jsonToObject(addResponse.body.toString())
            val delResponse = deleteFavouritesByUserId(addedFavourite.userid)

            assertEquals(204, delResponse.status)

            clearDB()
        }

        @Test
        fun `delete favourites by non-existing user id`() {
            val delResponse = deleteFavouritesByUserId(nonexisitingid)
            assertEquals(404, delResponse.status)

        }

        @Test
        fun `delete favourites by existing activity id`() {
            val addResponse = addFavourite(favvaliduserid1, favvalidactivityid1)
            val addedFavourite : Favourite = jsonToObject(addResponse.body.toString())
            val delResponse = deleteFavouritesByActivityId(addedFavourite.activityid)

            assertEquals(204, delResponse.status)

            clearDB()
        }

        @Test
        fun `delete favourites by non-existing activity id`() {
            val delResponse = deleteFavouritesByActivityId(nonexisitingid)
            assertEquals(404, delResponse.status)
        }
    }



}