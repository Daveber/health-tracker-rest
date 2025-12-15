package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.Favourite
import ie.setu.domain.Goal
import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Favourites
import ie.setu.domain.db.Goals

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToFavourite(it: ResultRow) = Favourite(
    id = it[Favourites.id],
    userid = it[Favourites.userId],
    activityid = it[Favourites.activityId]
)

fun mapToGoal(it: ResultRow) = Goal(
    id = it[Goals.id],
    userid = it[Goals.userid],
    targetCalories = it[Goals.targetCalories],
    recommendedid = it[Goals.recommendedid]
)