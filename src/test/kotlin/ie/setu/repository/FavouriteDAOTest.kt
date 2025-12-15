package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.Favourite
import ie.setu.domain.db.Favourites
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FavouriteDAO
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
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested

/** example data to create users and activities **/
val sampleuser1 = users[0]
val sampleuser2 = users[1]
val sampleuser3 = users[2]

val sampleactivity1 = activities[0]
val sampleactivity2 = activities[1]

val samplefavourite1 = favourites[0]
val samplefavourite2 = favourites[1]
val samplefavourite3 = favourites[2]


class FavouritesDAOTest {

    /**
     * populates the Favourite table by creating entries for users, activities and favourites returns the [FavouriteDAO]
     */
    internal fun populateFavouriteTable(): FavouriteDAO {
        //SchemaUtils.create(Favourites)
        val favouriteDAO = FavouriteDAO()
        val activityDAO = ActivityDAO()
        val userDAO = UserDAO()

        userDAO.save(sampleuser1)
        userDAO.save(sampleuser2)
        userDAO.save(sampleuser3)

        activityDAO.save(sampleactivity1)
        activityDAO.save(sampleactivity2)

        favouriteDAO.save(samplefavourite1)
        favouriteDAO.save(samplefavourite2)
        favouriteDAO.save(samplefavourite3)

        return favouriteDAO
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
    inner class CreateFavourites {
        @Test
        fun `create users, activities and add to favourites`(){
            transaction {

                val favouriteDAO = populateFavouriteTable()

                assertEquals(3, favouriteDAO.getAll().size)
                assertEquals(samplefavourite1, favouriteDAO.findByFavouriteId(samplefavourite1.id))
                assertEquals(listOf<Favourite>(samplefavourite1), favouriteDAO.findByUserId(sampleuser1.id))
                assertEquals(listOf<Favourite>(samplefavourite1, samplefavourite3), favouriteDAO.findByActivityId(sampleactivity1.id))
            }
        }
    }

    @Nested
    inner class ReadFavourites {
        @Test
        fun `getting all favourites from populated table returns all rows`() {
            transaction {

                val favouriteDAO = populateFavouriteTable()

                assertEquals(3, favouriteDAO.getAll().size)
            }
        }

        @Test
        fun `get favourite by id that doesnt exist, results in no favourite returned`() {
            transaction {
                val favouriteDAO = populateFavouriteTable()

                assertEquals(null, favouriteDAO.findByFavouriteId(5))
            }
        }

        @Test
        fun `get all favourites over empty table returns none`() {
            transaction {
                SchemaUtils.create(Favourites)
                val favouriteDAO = FavouriteDAO()

                assertEquals(0, favouriteDAO.getAll().size)
            }
        }

        @Test
        fun `get favourite by non exisiting user id returns nothing`() {
            transaction {
                val favouriteDAO = populateFavouriteTable()

                assertEquals(emptyList<Favourite>(), favouriteDAO.findByUserId(100))
            }
        }

        @Test
        fun `get favourites by non exisiting activity id return nothing`() {
            transaction {
                val favouriteDAO = populateFavouriteTable()

                assertEquals(emptyList<Activity>(), favouriteDAO.findByActivityId(100))
            }
        }
    }

    @Nested
    inner class DeleteFavourites {

        @Test
        fun `delete favourites by favourites id`() {
            transaction {
                val favouriteDAO = populateFavouriteTable()

                assertEquals(3, favouriteDAO.getAll().size)
                favouriteDAO.deletebyId(sampleuser1.id)
                assertEquals(2, favouriteDAO.getAll().size)
            }
        }

        @Test
        fun `delete favourites associated with user id`() {
            transaction {
                val favouriteDAO = populateFavouriteTable()

                assertEquals(listOf<Favourite>(samplefavourite1), favouriteDAO.findByUserId(sampleuser1.id))
                favouriteDAO.deleteAllFavouritesByUserId(sampleuser1.id)
                assertEquals(emptyList<Favourite>(), favouriteDAO.findByUserId(sampleuser1.id))
            }
        }

        @Test
        fun `delete favourites associated with activity id`() {
            transaction {
                val favouriteDAO = populateFavouriteTable()

                assertEquals(listOf<Favourite>(samplefavourite2), favouriteDAO.findByActivityId(sampleactivity2.id))
                favouriteDAO.deleteAllFavouritesByActivityId(sampleactivity2.id)
                assertEquals(emptyList<Favourite>(), favouriteDAO.findByActivityId(sampleactivity2.id))
            }
        }
    }
}