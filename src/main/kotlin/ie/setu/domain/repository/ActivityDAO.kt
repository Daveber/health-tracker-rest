package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it)) }
            }
            return activitiesList
    }

    fun findByActivityId(id: Int): Activity? {
        return transaction {
            Activities
                .selectAll().where(Activities.id eq id)
                .map { mapToActivity(it)}
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Activity> {
            return transaction {
                Activities
                    .selectAll().where{Activities.userId eq userId}
                    .map { mapToActivity(it) }
            }
        }

    fun save(activity: Activity) {
        transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[started] = activity.started
                it[calories] = activity.calories
                it[userId] = activity.userId
            }
        }
    }
}