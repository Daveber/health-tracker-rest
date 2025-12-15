package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.Favourite
import ie.setu.domain.Goal
import ie.setu.domain.User
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FavouriteDAO
import ie.setu.domain.repository.GoalDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import ie.setu.utils.jsonToObject

object GoalController {

    private val goalDAO = GoalDAO()

    /**
     * get all goals
     */
    fun getAllGoals(ctx: Context) {
        val goals = goalDAO.getAll()

        if (goals.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goals)
    }

    /**
     * get Goal by Id
     */
    fun getGoalById(ctx: Context) {
        val goal = goalDAO.findByGoalId(ctx.pathParam("goal-id").toInt())
        if (goal != null) {
            ctx.json(goal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * get Goal by corresponding user id
     */
    fun getGoalByUserId(ctx: Context) {
        val goal = goalDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (goal != null) {
            ctx.json(goal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * add Goal
     */
    fun addGoal(ctx: Context) {
        val goal : Goal = jsonToObject(ctx.body())
        val goalId = goalDAO.save(goal)
        if (goalId != null) {
            ctx.json(goal)
            ctx.status(201)
        }
    }

    /**
     * deleteGoal by id
     */
    fun deleteGoal(ctx: Context) {
        if (goalDAO.deleteById(ctx.pathParam("goal-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * update goal by id
     */
    fun updateGoal(ctx: Context) {
        val newGoal : Goal = jsonToObject(ctx.body())
        if ((goalDAO.update(ctx.pathParam("goal-id").toInt(), newGoal)) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * get Recommended activity based on target calories
     */
    fun getRecommended(ctx: Context) {
        val recommended = goalDAO.getRecommendation(ctx.pathParam("target-cal").toInt())
        if (recommended != null) {
            ctx.json(recommended)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Get Recommended activity based on target calories
     */
    fun getRecommendedId(ctx: Context) {
        val recommendedId = goalDAO.getRecommendationForGoal(ctx.pathParam("goal-id").toInt())
        if (recommendedId != null) {
            ctx.json(recommendedId)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
}