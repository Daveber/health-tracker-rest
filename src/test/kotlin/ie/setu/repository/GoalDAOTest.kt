package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.Favourite
import ie.setu.domain.Goal
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Favourites
import ie.setu.domain.db.Goals
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FavouriteDAO
import ie.setu.domain.repository.GoalDAO
import ie.setu.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.TestDatabaseConfig
import ie.setu.helpers.activities
import ie.setu.helpers.favourites
import ie.setu.helpers.goals
import ie.setu.helpers.nonexisitingid
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested

val sample_user1 = users[0]
val sample_user2 = users[1]
val sample_user3 = users[2]

val sample_activity1 = activities[0]
val sample_activity2 = activities[1]

val sample_goal1 = goals[0]
val sample_goal2 = goals[1]

class GoalDAOTest {

    internal fun populateGoalTable(): GoalDAO {

        val userDAO = UserDAO()
        val activityDAO = ActivityDAO()
        val goalDAO = GoalDAO()

        userDAO.save(sample_user1)
        userDAO.save(sample_user2)
        userDAO.save(sample_user3)

        activityDAO.save(sample_activity1)
        activityDAO.save(sample_activity2)

        goalDAO.save(sample_goal1)
        goalDAO.save(sample_goal2)

        return goalDAO
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
    inner class createGoals {

        @Test
        fun `create users, activities and create a goal`() {
            transaction {
                val goalDAO = populateGoalTable()
                val recommendedActivity = goalDAO.getRecommendation(100)

                goalDAO.save(Goal(userid = 3, targetCalories = 100, recommendedid = recommendedActivity.id, id = 3))

                assertEquals(3, goalDAO.getAll().size)
                //assertEquals(2, goalDAO.findByGoalId(3)?.recommendedid)
            }
        }

        @Nested
        inner class readGoals {

            @Test
            fun `get all goals when they exist`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    assertEquals(2, goalDAO.getAll().size)
                    assertEquals(listOf<Goal>(sample_goal1, sample_goal2), goalDAO.getAll())
                }
            }

            @Test
            fun `get all goals when they don't exist`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    goalDAO.deleteById(sample_goal1.id)
                    goalDAO.deleteById(sample_goal2.id)

                    assertEquals(0, goalDAO.getAll().size)
                }
            }

            @Test
            fun `find goal by an existing goal id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    assertEquals(sample_goal1, goalDAO.findByGoalId(sample_goal1.id))
                }

            }

            @Test
            fun `find goal by non-existing goal id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    assertEquals(null, goalDAO.findByGoalId(nonexisitingid))
                }

            }

            @Test
            fun `find goal by existing user-id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    assertEquals(sample_goal1, goalDAO.findByUserId(sample_user1.id))
                }
            }

            @Test
            fun `find goal by non-existing user-id`() {
                val goalDAO = populateGoalTable()

                assertEquals(null, goalDAO.findByUserId(nonexisitingid))
            }

            @Test
            fun `get Activity recommendation`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    assertEquals(activity1, goalDAO.getRecommendation(150)) //should get first activity back
                    assertEquals(activity2, goalDAO.getRecommendation(120)) //should get second activity back
                }
            }

            @Test
            fun `get Activity recommendation id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    assertEquals(activity1.id, goalDAO.getRecommendationId(150)) //should get first activity back
                    assertEquals(activity2.id, goalDAO.getRecommendationId(120)) //should get second activity back
                }
            }
        }

        @Nested
        inner class updateGoals {

            @Test
            fun `update goal by existing id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    goalDAO.update(sample_goal1.id, Goal(1, 2, 160, 2))
                    assertEquals(Goal(1, 2, 160, 2), goalDAO.findByGoalId(sample_goal1.id))
                }

            }

            @Test
            fun `update goal by non-existing id`() {
                transaction {
                    assertEquals(0, GoalDAO().update(nonexisitingid, sample_goal1))
                }


            }
        }

        @Nested
        inner class deleteGoals {

            @Test
            fun `delete goal by existing-id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    goalDAO.deleteById(sample_goal1.id)
                    assertEquals(1, goalDAO.getAll().size)
                }
            }

            @Test
            fun `delete goal by non-existing id`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    goalDAO.deleteById(nonexisitingid)
                    assertEquals(2, goalDAO.getAll().size)
                }
            }

            @Test
            fun `delete goal by existing userid`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    goalDAO.deleteByUserId(sample_user1.id)
                    assertEquals(1, goalDAO.getAll().size)
                }
            }

            @Test
            fun `delete goal by non-existing userid`() {
                transaction {
                    val goalDAO = populateGoalTable()

                    goalDAO.deleteByUserId(nonexisitingid)
                    assertEquals(2, goalDAO.getAll().size)
                }
            }
        }
    }
}

