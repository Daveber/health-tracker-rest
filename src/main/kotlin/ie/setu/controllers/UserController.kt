package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object UserController {

    private val userDao = UserDAO()
    private val mapper = jacksonObjectMapper()

    /** Get all users **/
    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    /** Get User by user id **/
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findUserById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    /** Get user by user email **/
    fun getUserbyUserEmail(ctx: Context) {
        val email = userDao.findUserByEmail(ctx.pathParam("email"))
        if (email != null) {
            ctx.json(email)
        }
    }

    /** Add User **/
    fun addUser(ctx: Context){
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    /** Update user **/
    fun updateUser(ctx: Context){
        val userid = ctx.pathParam("user-id").toInt()
        val newuser = mapper.readValue<User>(ctx.body())
        userDao.update(userid, newuser)
    }

    /** Delete user **/
    fun deleteUser(ctx: Context){
        userDao.delete(ctx.pathParam("user-id").toInt())
    }
}