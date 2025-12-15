package ie.setu.controllers

import ie.setu.config.DBConfig
import ie.setu.controllers.ActivityController.deleteActivity
import ie.setu.domain.Activity
import ie.setu.domain.Goal
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Activities.userId
import ie.setu.domain.db.Goals
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.GoalDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.TestDatabaseConfig
import ie.setu.helpers.activities
import ie.setu.helpers.nonexisitingid
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
import ie.setu.repository.exampleuser1
import ie.setu.repository.sample_activity1
import ie.setu.repository.sample_goal1
import ie.setu.repository.sample_goal2
import ie.setu.repository.sampleuser1
import ie.setu.repository.user1
import ie.setu.repository.user2
import ie.setu.utils.jsonToObject
import io.fabric8.openshift.api.model.config.v1.IdentityProviderFluent
import org.junit.jupiter.api.Assertions.assertEquals
import kong.unirest.core.JsonNode
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime
import kong.unirest.core.HttpResponse
import kong.unirest.core.Unirest
import org.apache.maven.artifact.InvalidArtifactRTException
import org.bouncycastle.oer.its.ieee1609dot2.EndEntityType.app
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertNotEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoalControllerTest {

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    internal fun populateUserTable(): UserDAO {
        //SchemaUtils.create(Users)

        val userDAO = UserDAO()

        userDAO.save(user1) //id 1
        userDAO.save(user2) //id 2

        return userDAO
    }

    internal fun populateActivityTable(): ActivityDAO {
        //SchemaUtils.create(Activities)

        val activityDAO = ActivityDAO()

        activityDAO.save(activity1) //activity id 1, user id 1
        activityDAO.save(activity2) //activity id 2, user id 2

        return activityDAO
    }

    internal fun populateGoalTable(): GoalDAO {
        //SchemaUtils.create(Goals)

        val goalDAO = GoalDAO()

        goalDAO.save(sample_goal1)
        goalDAO.save(sample_goal2)

        return goalDAO
    }

    /**
     * Helper function to add goal
     */
    private fun addGoal(id: Int, userId: Int, targetCalories: Int, recommendedId: Int): HttpResponse<JsonNode> {
        return Unirest.post( origin + "/api/goals")
            .body("""
                {
                    "id": "$id",
                    "userid": "$userId",
                    "targetCalories": "$targetCalories",
                    "recommendedid": "$recommendedId"
                }
            """.trimIndent())
        .asJson()
    }

    /**
     * Helper function to get all goals
     */
    private fun getAllGoals(): HttpResponse<String> {
        return Unirest.get(origin + "/api/goals").asString()
    }

    /**
     * Helper function to retrieve goal with corresponding id
     */
    private fun getGoalById(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/goals/${id}").asString()
    }

    /**
     * Helper function to get goals with corresponding user-id
     */
    private fun getGoalByUserId(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}/goals").asString()
    }

    /**
     * Helper function to get activity recommendation for goal
     */
    private fun getRecommendation(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/goals/${id}/recommended").asString()
    }

    /**
     * Helper function to update Goal
     */
    private fun updateGoal(goalid: Int, userId: Int, targetCalories: Int, recommendedId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/goals/${goalid}")
            .body("""
                {
                    "userid": "$userId",
                    "targetCalories": "$targetCalories",
                    "recommendedid": "$recommendedId"
                }
            """.trimIndent())
            .asJson()
    }

    /**
     * Helper function to delete goal
     */
    private fun deleteGoal(goalid: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/goals/${goalid}").asString()
    }


    companion object {
        @BeforeAll
        @JvmStatic
        fun setupInMemoryDatabase(){
            TestDatabaseConfig.connect()
        }
    }

    @BeforeEach
    fun resetDatabase(){
        TestDatabaseConfig.reset()
    }

    @Nested
    inner class CreateGoals {

        @Test
        fun `add goal with correct details`() {
                populateUserTable()
                populateActivityTable()
                populateGoalTable()


                val addedResponse = addGoal(sample_goal1.id,exampleuser1.id,sample_goal1.targetCalories,sample_activity1.id)
                assertEquals(201, addedResponse.status)

                val addedGoal: Goal = jsonToObject(addedResponse.body.toString())
                assertEquals(sample_goal1.userid, addedGoal.userid)
                assertEquals(sample_goal1.targetCalories, addedGoal.targetCalories)
                assertEquals(sample_goal1.userid, addedGoal.userid)

                assertEquals(200, getAllGoals().status)
        }
    }

    @Nested
    inner class ReadGoals {

        @Test
        fun `get all goals when they exist`(){
                populateUserTable()
                populateActivityTable()
                populateGoalTable()

                addGoal(sample_goal1.id, exampleuser1.id, sample_goal1.targetCalories, sample_activity1.id)

                assertEquals(200, getAllGoals().status)
        }

        @Test
        fun `get all goals when they dont exist`(){
                assertEquals(404, getAllGoals().status)
        }

        @Test
        fun `get goal by exisiting id`(){

            populateUserTable()
            populateActivityTable()
            populateGoalTable()

            val addedResponse = addGoal(sample_goal1.id, exampleuser1.id, sample_goal1.targetCalories, sample_activity1.id)
            val addedGoal: Goal = jsonToObject(addedResponse.body.toString())

            assertEquals(200, getGoalById(addedGoal.id).status)
        }

        @Test
        fun `get goal by non-existing id`(){
        assertEquals(404, getGoalByUserId(nonexisitingid).status)
        }

        @Test
        fun `get goal by existing user id`(){
            populateUserTable()
            populateActivityTable()
            populateGoalTable()

            val addedResponse = addGoal(sample_goal1.id, exampleuser1.id, sample_goal1.targetCalories, sample_activity1.id)
            val addedGoal: Goal = jsonToObject(addedResponse.body.toString())

            assertEquals(200, getGoalByUserId(addedGoal.userid).status)
        }

        @Test
        fun `get goal by non-exisiting id`() {
            assertEquals(404, getGoalByUserId(nonexisitingid).status)
        }

        @Test
        fun `get activity recommendation for goal`() {

            populateUserTable()
            populateActivityTable()
            populateGoalTable()

            val recommendedResponse = getRecommendation(sample_goal1.id)
            val Recommended : Int = recommendedResponse.body.toInt()

            addGoal(sample_goal1.id,exampleuser1.id, sample_goal1.targetCalories, Recommended)
        }
    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `update goal by existing id`(){

            populateUserTable()
            populateActivityTable()
            populateGoalTable()

            val updatedGoal = updateGoal(sample_goal1.id, exampleuser1.id, 1000, sample_activity1.id)

            assertEquals(204, updatedGoal.status)
        }

        @Test
        fun `update goal by non-existing id`(){

            assertEquals(404, updateGoal(sample_goal1.id, exampleuser1.id, 1000, sample_activity1.id).status)
        }
    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `delete goal by existing id`(){
            populateUserTable()
            populateActivityTable()
            populateGoalTable()

            assertEquals(204, deleteGoal(sample_goal1.id).status)
        }

        @Test
        fun `delete goal by non-existing id`(){
            assertEquals(404, deleteGoal(sample_goal1.id).status)
        }
    }
}