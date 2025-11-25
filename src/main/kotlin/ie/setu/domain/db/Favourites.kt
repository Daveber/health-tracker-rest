package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Favourites : Table("favourites") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val activityId = integer("activity_id").references(Activities.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Favourites.id, name = "PK_Favourites_ID")
}