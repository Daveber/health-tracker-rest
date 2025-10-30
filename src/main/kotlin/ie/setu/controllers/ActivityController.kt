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
import ie.setu.utils.jsonToObject

object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    /** Get all activities **/
    fun getAllActivities(ctx: Context) {

        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val activities = activityDAO.getAll()

        if (activities.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString( activityDAO.getAll()))
    }

    /** Get activities by user ID **/
    fun getActivitiesByUserId(ctx: Context) {

            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())

            if (activities.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

                ctx.json(mapper.writeValueAsString(activities))
                ctx.status(200)
            } else {
                ctx.status(404)
            }
    }

    /** Add Activity **/
    fun addActivity(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val activity = jsonToObject<Activity>(ctx.body())

        activityDAO.save(activity)
        ctx.json(activity)
        ctx.status(201)
    }

    /** Delete activities by associated user ID **/
    fun deleteActivitiesByUserId(ctx: Context) {
        if (activityDAO.deleteAllAssociatedByUserId(ctx.pathParam("user-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /** Delete activity by ID **/
    fun deleteActivity(ctx: Context){
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /** Update activity by ID **/
    fun updateActivity(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val newActivity = jsonToObject<Activity>(ctx.body())

        activityDAO.update(ctx.pathParam("activity-id").toInt(), newActivity)

        val foundActivity : Activity = jsonToObject(ctx.body())

        if ((activityDAO.update(ctx.pathParam("activity-id").toInt(), foundActivity)) != 0 ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /** Get activity by activity ID **/
    fun getActivityById(ctx: Context){
        val activity = activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())

        if (activity != null) {
            ctx.json(activity)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
}