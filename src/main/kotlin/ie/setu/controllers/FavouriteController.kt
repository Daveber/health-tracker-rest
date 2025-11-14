package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
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

}