package ie.setu.config

import io.javalin.Javalin
import ie.setu.controllers.ActivityController
import ie.setu.controllers.UserController
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

        /** Vue Routes **/
        app.get("/", VueComponent("<home-page></home-page>"))
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
