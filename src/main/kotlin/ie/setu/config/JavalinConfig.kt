package ie.setu.config

import io.javalin.Javalin
import ie.setu.controllers.ActivityController
import ie.setu.controllers.FavouriteController
import ie.setu.controllers.UserController
import ie.setu.domain.Favourite
import ie.setu.utils.jsonObjectMapper
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

//
class JavalinConfig {

        val app = Javalin.create(
            { config ->
                config.jsonMapper(JavalinJackson(jsonObjectMapper()))
                config.staticFiles.enableWebjars()
                config.vue.vueInstanceNameInJs = "app"
            }
        ).apply {
            exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }

        fun startJavalinService(): Javalin {
            app.start(getRemoteAssignedPort())
            registerRoutes(app)
            return app
        }

    private fun registerRoutes(app: Javalin) {

        /** User Endpoints **/
        app.get("/api/users", UserController::getAllUsers)
        app.get("/api/users/{user-id}", UserController::getUserByUserId)
        app.post("/api/users", UserController::addUser)
        app.get("/api/users/email/{email}", UserController::getUserbyUserEmail)
        app.patch("/api/users/{user-id}", UserController::updateUser)
        app.delete("/api/users/{user-id}", UserController::deleteUser)

        /** Activity Endpoints **/
        app.get("/api/activities", ActivityController::getAllActivities)
        app.post("/api/activities", ActivityController::addActivity)
        app.get("/api/users/{user-id}/activities", ActivityController::getActivitiesByUserId)
        app.delete("/api/users/{user-id}/activities", ActivityController::deleteActivitiesByUserId)
        app.delete("/api/activities/{activity-id}", ActivityController::deleteActivity)
        app.patch("/api/activities/{activity-id}", ActivityController::updateActivity)
        app.get("/api/activities/{activity-id}", ActivityController::getActivityById)

        /** Favourite Endpoints **/
        app.get("/api/favourites", FavouriteController::getAllFavourites)
        app.get("/api/favourites/{favourite-id}", FavouriteController::getFavouriteByFavouriteId)
        app.get("/api/users/{user-id}/favourites", FavouriteController::getFavouritesByUserId)
        app.get("/api/activities/{activity-id}/favourites", FavouriteController::getFavouritesByActivityId)
        app.post("/api/favourites", FavouriteController::addFavourite)
        app.delete("/api/favourites/{favourite-id}", FavouriteController::deleteFavouriteById)
        app.delete("/api/users/{user-id}/favourites", FavouriteController::deleteFavouritesByUserId)
        app.delete("/api/activities/{activity-id}/favourites", FavouriteController::deleteFavouriteByActivityId)

        /** Vue Routes **/
        app.get("/", VueComponent("<home-page></home-page>"))
        app.get("/users", VueComponent("<user-overview></user-overview>"))
        app.get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
        app.get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
        app.get("/activities", VueComponent("<activity-overview></activity-overview>"))
        app.get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 8080
    }

    fun getJavalinService(): Javalin {
        registerRoutes(app)
        return app
    }

}
