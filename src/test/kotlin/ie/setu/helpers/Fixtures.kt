package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Favourite
import ie.setu.domain.Goal
import ie.setu.domain.User
import org.jetbrains.exposed.sql.jodatime.dateTimeParam
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.ie", id = 3),
    User(name = "Carol Singer", email = "carol@singer.ie", id = 4)
)

val validActivityId = 1
val validDescription = "Cycling"
val validDuration = 1.30
val validCalories = 200
val validDateTime = DateTime.parse("2020-04-25T12:30:00.00")
val validUserId = 1

val newValidActivity = Activity(validActivityId, validDescription,validDuration, validCalories, validDateTime, validUserId)

val activities = arrayListOf<Activity>(
    Activity(1, "Running", 1.30, 152, validDateTime, 1),
    Activity(2, "Swimming", 1.30, 147, validDateTime, 2),
    Activity(3, "Rowing", 1.30, 120, validDateTime, 1),
    Activity(4, "Archery", 1.30, 120, validDateTime, 2)
)

val favourites = arrayListOf<Favourite>(
    Favourite(userid = 1, activityid = 1, id = 1),
    Favourite(userid = 2,  activityid = 2, id = 2),
    Favourite(userid = 3, activityid = 1, id = 3)
)

val goals = arrayListOf<Goal>(
    Goal(userid = 1, targetCalories = 100, recommendedid = 1, id =1),
    Goal(userid = 2, targetCalories = 200, recommendedid = 2, id =2),
)

val favvaliduserid1 = 1
val favvaliduserid2 = 2

val favvalidactivityid1 = 1
val favvalidactivityid2 = 2

val nonexisitingid = -1