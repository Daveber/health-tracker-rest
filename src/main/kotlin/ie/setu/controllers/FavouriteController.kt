package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.Favourite
import ie.setu.domain.User
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FavouriteDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import ie.setu.utils.jsonToObject

object FavouriteController {

    private val userDAO = UserDAO()
    private val activityDAO = ActivityDAO()
    private val favouriteDAO = FavouriteDAO()

    /** Get all favourites **/
    fun getAllFavourites(ctx: Context) {
        val favourites = favouriteDAO.getAll()

        if (favourites.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }

        ctx.json(favourites)
    }

    /** Get favourite with specified favourite id **/
    fun getFavouriteByFavouriteId(ctx: Context) {
        val favourite = favouriteDAO.findByFavouriteId(ctx.pathParam("favourite-id").toInt())
        if (favourite != null) {
            ctx.json(favourite)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /** Get all favourites with specified user id **/
    fun getFavouritesByUserId(ctx: Context) {
        val favourites = favouriteDAO.findByUserId(ctx.pathParam("user-id").toInt())

        if (favourites.isNotEmpty()) {
            ctx.json(favourites)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /** Get all favourites with specified Activity id **/
    fun getFavouritesByActivityId(ctx: Context) {
        val favourites = favouriteDAO.findByActivityId(ctx.pathParam("activity-id").toInt())

        if (favourites.isNotEmpty()) {
            ctx.json(favourites)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /** Add favourite **/
    fun addFavourite(ctx: Context) {
        val favourite : Favourite = jsonToObject(ctx.body())
        val favouriteId = favouriteDAO.save(favourite)
        if (favouriteId != null) {
            favourite.id = favouriteId
            ctx.json(favourite)
            ctx.status(201)
        }
    //  else {
//            ctx.status(400)
//        }
    }

    /** Delete favourite with specified favourite id **/
    fun deleteFavouriteById(ctx: Context) {
        if (favouriteDAO.deletebyId(ctx.pathParam("favourite-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /** Delete all favourites with specified user id**/
    fun deleteFavouritesByUserId(ctx: Context) {
        if (favouriteDAO.deleteAllFavouritesByUserId(ctx.pathParam("user-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /** Delete all Favourites with specified activity id **/
    fun deleteFavouriteByActivityId(ctx: Context) {
        if (favouriteDAO.deleteAllFavouritesByActivityId(ctx.pathParam("activity-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}