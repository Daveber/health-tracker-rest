package ie.setu.domain.repository

import ie.setu.domain.Favourite
import ie.setu.domain.db.Favourites
import ie.setu.utils.mapToFavourite
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FavouriteDAO {

    /**
     * Get all favourites
     * @return list of favourites
     */
    fun getAll(): ArrayList<Favourite> {
        val favouritesList: ArrayList<Favourite> = arrayListOf()
        transaction {
            Favourites.selectAll().map {
                favouritesList.add(mapToFavourite(it)) }
        }
        return favouritesList
    }

    /**
     * get favourite by [id]
     * @return favourite
     */
    fun findByFavouriteId(id: Int): Favourite? {
        return transaction {
            Favourites
                .selectAll().where(Favourites.id eq id)
                .map { mapToFavourite(it) }
                .firstOrNull()
        }
    }

    /**
     * get favourite by [userId]
     */

    fun findByUserId(userId: Int): List<Favourite> {
        return transaction {
            Favourites
                .selectAll().where{ Favourites.userId eq userId }
                .map { mapToFavourite(it) }
        }
    }

    /**
     * find favourite by [activityId]
     * @return list of favourites
     */
    fun findByActivityId(activityId: Int): List<Favourite> {
        return transaction {
            Favourites
                .selectAll().where{ Favourites.activityId eq activityId }
                .map { mapToFavourite(it) }
        }
    }

    /**
     * save favourite
     * @return id of added favourite
     */
    fun save(favourite: Favourite): Int? {
        return transaction {
            Favourites.insert {
                it[userId] = favourite.userid
                it[activityId] = favourite.activityid
            } get Favourites.id
        }
    }

    /**
     * delete all favourites with associated [userId]
     */
    fun deleteAllFavouritesByUserId(userId: Int?) : Int? {
        return transaction {
            Favourites.deleteWhere { Favourites.userId eq userId as Int }
        }
    }

    /**
     * delete all favourites with associated [activityId]
     */
    fun deleteAllFavouritesByActivityId(activityId: Int?) : Int? {
        return transaction {
            Favourites.deleteWhere { Favourites.activityId eq activityId as Int }
        }
    }

    /**
     * delete favourite with corresponding [id]
     */
    fun deletebyId(id: Int?) : Int? {
        return transaction {
            Favourites.deleteWhere { Favourites.id eq id as Int }
        }
    }
}