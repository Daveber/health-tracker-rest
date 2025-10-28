package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object UserController {

    private val userDao = UserDAO()
    private val mapper = jacksonObjectMapper() //user for old mapper before jsonToObject helper in JSONUtilities.kt

    /** Get all users **/
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()

        if (users.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(users)
    }

    /** Get User by user id **/
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findUserById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /** Get user by user email **/
    fun getUserbyUserEmail(ctx: Context) {
        val email = userDao.findUserByEmail(ctx.pathParam("email"))
        if (email != null) {
            ctx.json(email)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /** Add User **/
    fun addUser(ctx: Context) {
        val user : User = jsonToObject(ctx.body())
        val userId = userDao.save(user)
        if (userId != null) {
            user.id = userId
            ctx.json(user)
            ctx.status(201)
        }
    }

    /** Update user **/
    fun updateUser(ctx: Context) {
        val foundUser : User = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), user=foundUser)) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }

    }

    /** Delete user **/
    fun deleteUser(ctx: Context){
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}