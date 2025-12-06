package ie.setu.domain.repository


import ie.setu.domain.Activity
import ie.setu.domain.Goal
import ie.setu.domain.db.Goals
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.mapToGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class GoalDAO {

    /**
     * get all Goals
     */
    fun getAll(): ArrayList<Goal> {
        val goalslist: ArrayList<Goal> = arrayListOf()
        transaction {
            Goals.selectAll().map {
                goalslist.add(mapToGoal(it))
            }
        }
        return goalslist
    }

    /**
     * find goal by goal [id]
     */
    fun findByGoalId(id: Int): Goal? {
        return transaction {
            Goals
                .selectAll().where(Goals.id eq id)
                .map { mapToGoal(it) }
                .firstOrNull()
        }
    }

    /**
     * find goal by user [id]
     */
    fun findByUserId(id: Int): Goal? {
        return transaction {
            Goals.selectAll()
                .where(Goals.id eq id)
                .map{ mapToGoal(it)}
                .firstOrNull()
        }
    }

    /**
     * save goal and return [goal] id
     */
    fun save(goal: Goal): Int? {
        return transaction {
            Goals.insert {
                it[userid] = goal.id
                it[targetCalories] = goal.targetCalories
                it[recommendedid] = goal.recommendedid
            } get Goals.id
        }
    }

    /**
     * delete goal by [id]
     */
    fun deleteById(id: Int): Int? {
        return transaction {
            Goals.deleteWhere { Goals.id eq id }
        }
    }

    /**
     * delete goal by associated [userid]
     */
    fun deleteByUserId(userid: Int): Int? {
        return transaction {
            Goals.deleteWhere { Goals.userid eq userid }
        }
    }

    /**
     * get Activity recommendation based on [targetCalories]
     */
    fun getRecommendation(targetCalories: Int): Activity {
        return transaction {
            val activitiesList = ActivityDAO().getAll()
            val closestActivity = activitiesList.minByOrNull { it.calories - targetCalories }
            activitiesList.first { it.calories == closestActivity?.calories }
        }
    }

    /**
     * update an existing goals by [id] with [goal] parameters
     */
    fun update(id: Int, goal: Goal): Int? {
        return transaction {
            Goals.update({ Goals.id eq id }) {
                it[userid] = goal.userid
                it[targetCalories] = goal.targetCalories
                it[recommendedid] = goal.recommendedid
            }
        }
    }
}