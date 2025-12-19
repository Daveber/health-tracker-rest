package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    /**
     * get all activities
     * @return list of activities
     */
    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it)) }
            }
            return activitiesList
    }

    /**
     * find activity id [id]
     * @return activity
     */
    fun findByActivityId(id: Int): Activity? {
        return transaction {
            Activities
                .selectAll().where(Activities.id eq id)
                .map { mapToActivity(it)}
                .firstOrNull()
        }
    }

    /**
     * find activity by [userId]
     * @return list of activities
     */
    fun findByUserId(userId: Int): List<Activity> {
            return transaction {
                Activities
                    .selectAll().where{Activities.userId eq userId}
                    .map { mapToActivity(it) }
            }
        }

    /**
     * save activity
     * @return added activity id
     */
    fun save(activity: Activity): Int? {
        return transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[started] = activity.started
                it[calories] = activity.calories
                it[userId] = activity.userId
            } get Activities.id
        }
    }

    /**
     * delete acc activities associated with a specific user [id]
     */
    fun deleteAllAssociatedByUserId(id: Int?) : Int? {
        return transaction {
            Activities.deleteWhere { Activities.userId eq id as Int }
        }
    }

    /**
     * delete activity by activity [id]
     */
    fun deleteByActivityId(id: Int?) : Int? {
        return transaction {
            Activities.deleteWhere { Activities.id eq id as Int}
        }
    }

    /**
     * Update activity with specific activity [id]
     */
    fun update(id: Int, activity: Activity) : Int? {
       return transaction {
            Activities.update({ Activities.id eq id }) {
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
        }
       }
    }
}