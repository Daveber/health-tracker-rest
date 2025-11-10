package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    /** get all activities **/
    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it)) }
            }
            return activitiesList
    }

    /** find activity by id test written **/
    fun findByActivityId(id: Int): Activity? {
        return transaction {
            Activities
                .selectAll().where(Activities.id eq id)
                .map { mapToActivity(it)}
                .firstOrNull()
        }
    }

    /** find activities by user id it test written **/
    fun findByUserId(userId: Int): List<Activity> {
            return transaction {
                Activities
                    .selectAll().where{Activities.userId eq userId}
                    .map { mapToActivity(it) }
            }
        }

    /** save Activity Test written**/
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

//    /** Get activities associated with user id **/
//    fun getActivities(id: Int): List<Activity> {
//        transaction {
//            return
//        }
//    }

    /** Delete Activities associated with a user id **/
    fun deleteAllAssociatedByUserId(id: Int?) : Int? {
        return transaction {
            Activities.deleteWhere { Activities.userId eq id as Int }
        }
    }

    /** Delete Activity with specific Activity id test written**/
    fun deleteByActivityId(id: Int?) : Int? {
        return transaction {
            Activities.deleteWhere { Activities.id eq id as Int}
        }
    }

    /** Update Activity with specific ID test written **/
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