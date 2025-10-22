package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.activities
import ie.setu.helpers.nonExistingEmail
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import ie.setu.domain.repository.ActivityDAO
import ie.setu.helpers.users
import ie.setu.helpers.validDateTime


/** users put the user from all tests to fixtures maybe? users can be accessed from other tests **/
val exampleuser1 = users[0]
val exampleuser2 = users[1]

/** activities**/
val activity1 = activities[0]
val activity2 = activities[1]
val activity3 = activities[2]

class ActivityDaoTest {

    internal fun populateActivityTable(): ActivityDAO {
        SchemaUtils.create(Activities)
        val activityDAO = ActivityDAO()
        val userDAO = UserDAO()

        userDAO.save(exampleuser1) //saving example users change this ???
        userDAO.save(exampleuser2)

        activityDAO.save(activity1)
        activityDAO.save(activity2)
        activityDAO.save(activity3)
        return activityDAO
    }

    internal fun populateUsersTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(exampleuser1)
        userDAO.save(exampleuser2)
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
    inner class CreateActivities {
        @Test
        fun `create 2 users id 1 and 2 multiple activities added to the table can be retrieved successfully`() {
            transaction {

                val activityDAO = populateActivityTable()

                assertEquals(3, activityDAO.getAll().size)
                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            }
        }
    }

    @Nested
    inner class ReadActivities {
        @Test
        fun `retrieving activity by activity id successfully`() {
            transaction {

                val activityDAO = populateActivityTable()

                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            }
        }

        @Test
        fun `retreiving list of activities by userid successfully`() {
            transaction {

                val activityDAO = populateActivityTable()

                assertEquals(listOf<Activity>(activity1, activity3), activityDAO.findByUserId(user1.id))
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating activity by activity id successfully`() {
            transaction {

                val activityDAO = populateActivityTable()

                val updatedActivity = Activity(1, "Changed", 3.40, 120, validDateTime, 1)
                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                activityDAO.update(activity1.id, updatedActivity)
                assertEquals(updatedActivity, activityDAO.findByActivityId(activity1.id))
            }
        }
    }

    @Nested
    inner class DeleteActivities {
        @Test
        fun `deleting activity by id successfully`() {
            transaction {

                val activityDAO = populateActivityTable()

                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                activityDAO.delete(activity1.id)
                assertEquals(null, activityDAO.findByActivityId(activity1.id))
            }
        }

        @Test
        fun `deleting activities associated with user id`() {
            transaction {

                val activityDAO = populateActivityTable()

                assertEquals(listOf<Activity>(activity1, activity3), activityDAO.findByUserId(user1.id))
                activityDAO.delete(user1.id)
                assertEquals(emptyList<Activity>(), activityDAO.findByUserId(user1.id))

            }
        }
    }
}


