package ie.setu.controllers

import ie.setu.config.DBConfig
import ie.setu.controllers.ActivityController.deleteActivity
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Activities.userId
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.activities
import ie.setu.helpers.validActivityId
import ie.setu.helpers.validCalories
import ie.setu.helpers.validDateTime
import ie.setu.helpers.validDescription
import ie.setu.helpers.validDuration
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import ie.setu.helpers.validUserId
import ie.setu.repository.activity1
import ie.setu.repository.activity2
import ie.setu.repository.activity3
import ie.setu.repository.activity4
import ie.setu.repository.user1
import ie.setu.repository.user2
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions.assertEquals
import kong.unirest.core.JsonNode
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime
import kong.unirest.core.HttpResponse
import kong.unirest.core.Unirest
import org.bouncycastle.oer.its.ieee1609dot2.EndEntityType.app
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertNotEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityControllerTest {


    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    //copied from userDAOTest to provide ids for activities
    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1) //id 1
        userDAO.save(user2) //id 2
        return userDAO
    }

    //populate activities table for testing
    internal fun populateActivitiesTable(): ActivityDAO {
        SchemaUtils.create(Activities)
        val activitiesDAO = ActivityDAO()
        activitiesDAO.save(activity1) //user id 1, activity id 1
        activitiesDAO.save(activity2) //user id 2, activity id 2
        activitiesDAO.save(activity3) //user id 1, activity id 3
        activitiesDAO.save(activity4) //user id 2, activity id 4
        return activitiesDAO
    }


    /** Helper function to add activity to test database **/
    private fun addActivity (description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/activities")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\", \"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\"}")
            .asJson()
    }

    /** Helper function to get activity by activity id **/
    private fun getActivityById (id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/activities/${id}").asString()
    }

    /** Helper function to get activities by user id **/
    private fun getActivitiesByUserId(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}/activities").asString()
    }

    /** Helper function to get all activities **/
    private fun getAllActivities(): HttpResponse<String> {
        return Unirest.get(origin + "/api/activities").asString()
    }

    /** Helper function to delete activities by user id **/
    private fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/${id}/activities").asString()
    }

    /** Helper function to delete activity by activity id **/
    private fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/${id}").asString()
    }

    /** Helper function to update activity **/
    private fun updateActivity(id: Int, description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/$id")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\", \"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\"}")
            .asJson()
    }

    /** Helper function to add user to test database **/
    private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    /** Helper function to delete user from test database **/
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id").asString()
    }

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupInMemoryDatabase() {
            Database.connect(
                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                driver = "org.h2.Driver",
                user = "sa",
                password = ""
            )
            transaction {
                SchemaUtils.create(Activities)
            }
        }
    }


    @Nested
    inner class CreateActivities {

        @Test
        fun `add activity with correct details - returns 201 response` () {

            //Add user and verify response
            val addUserResponse = addUser(validName, validEmail)
            assertEquals(201, addUserResponse.status)
            val addedUser : User = jsonToObject(addUserResponse.body.toString())

            //Get activity without any activities existing
            val getResponseEmpty = getAllActivities()
            assertEquals(404, getResponseEmpty.status)

            //Add activity and verify response
            val addResponse = addActivity(validDescription, validDuration, validCalories, validDateTime, addedUser.id)
            assertEquals(201, addResponse.status)

            //Retrieving activities
            val getResponse = getAllActivities()
            assertEquals(200, getResponse.status)

            //Verifying activity body contents
            val retrievedActivity : Activity = jsonToObject(addResponse.body.toString())
            assertEquals(validDescription, retrievedActivity.description)
            assertEquals(validDuration, retrievedActivity.duration)
            assertEquals(validCalories, retrievedActivity.calories)
            assertEquals(validDateTime.toInstant(), retrievedActivity.started.toInstant())
            assertEquals(addedUser.id, retrievedActivity.userId)

            //Deleting activities and restoring db verifying return code
            val delResponse = deleteActivitiesByUserId(retrievedActivity.userId)
            assertEquals(204, delResponse.status)

            //Deleting user returning db to initial state
            val delUserResponse = deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `get activities by user id test returns 200 or 404 response` () {

            val getResponse = getActivitiesByUserId(validUserId)

            if (getResponse.status == 200) {
                val retrievedActivities : List<Activity> = jsonToObject(getResponse.body.toString())
                assertNotNull(retrievedActivities)
                assertNotEquals(0, retrievedActivities.size)
            } else {
                assertEquals(404, getResponse.status)
            }
        }

        @Test
        fun `Get all activities returns either 200 or 404 response` () {
            val getResponse = getAllActivities()
            if (getResponse.status == 200) {
                val retrievedActivities : List<Activity> = jsonToObject(getResponse.body.toString())
                assertNotEquals(0, retrievedActivities.size)
            } else {
                assertEquals(404, getResponse.status)
            }
        }

        @Test
        fun `Get activity by activity id` () {

            val addUserResponse = addUser(validName, validEmail)
            val addedUser : User = jsonToObject(addUserResponse.body.toString())
            assertEquals(201, addUserResponse.status)


            val addActivityResponse = addActivity(validDescription, validDuration, validCalories, validDateTime, addedUser.id)
            val addedActivity : Activity = jsonToObject(addActivityResponse.body.toString())
            assertEquals(201, addActivityResponse.status)

            val getResponse = getActivityById(addedActivity.id)
            assertEquals(200, getResponse.status)

            deleteActivityByActivityId(addedActivity.id)
            deleteActivitiesByUserId(addedUser.id)
        }
    }

    @Nested
    inner class UpdateActivity {
        //  patch( "/api/activities/:activity-id", HealthTrackerController::updateActivity)
    }

    @Nested
    inner class DeleteActivities {
        //   delete("/api/activities/:activity-id", HealthTrackerController::deleteActivityByActivityId)
        //   delete("/api/users/:user-id/activities", HealthTrackerController::deleteActivityByUserId)
    }


}