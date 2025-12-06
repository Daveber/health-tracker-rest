package ie.setu.domain.db

import ie.setu.domain.Activity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Goals : Table("goals") {
    val id = integer("id").autoIncrement()
    val userid = integer("userid").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val targetCalories = integer("target_calories")
    val recommendedid = integer("recommendedid").references(Activities.id, onDelete = ReferenceOption.CASCADE)
}