package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val mapper = jacksonObjectMapper()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findUserById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun getUserbyUserEmail(ctx: Context) {
        val email = userDao.findUserByEmail(ctx.pathParam("email"))
        if (email != null) {
            ctx.json(email)
        }
    }

    fun addUser(ctx: Context){
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun updateUser(ctx: Context){
        val userid = ctx.pathParam("user-id").toInt()
        val newuser = mapper.readValue<User>(ctx.body())
        userDao.update(userid, newuser)
    }

    fun deleteUser(ctx: Context){
        userDao.delete(ctx.pathParam("user-id").toInt())
    }


    /** Beginning of Activity Functions**/


    fun getAllActivities(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( activityDAO.getAll() ))
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findUserById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
            }
        }
    }

    fun addActivity(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.save(activity)
        ctx.json(activity)
    }

    /** delete activities by associated user ID TEST **/
    fun deleteActivitiesByUserId(ctx: Context) {
        activityDAO.deleteAllAssociatedByUserId(ctx.pathParam("user-id").toInt())
    }

    /** delete activity by id TEST **/
    fun deleteActivity(ctx: Context){
        activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt())
    }

    /** updateActivity by id TEST **/
    fun updateActivity(ctx: Context){
        val mapper = jacksonObjectMapper()
        .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val newActivity = mapper.readValue<Activity>(ctx.body())

        activityDAO.update(ctx.pathParam("activity-id").toInt(), newActivity)
    }

    /** get activity by activity id **/
    fun getActivityById(ctx: Context){
        activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
    }
}