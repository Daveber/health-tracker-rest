package ie.setu.config

import io.javalin.Javalin
import ie.setu.controllers.HealthTrackerController
import ie.setu.utils.jsonObjectMapper
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

//
class JavalinConfig {

        val app = Javalin.create(
            { config ->
                config.jsonMapper(JavalinJackson(jsonObjectMapper()))
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
        app.get("/api/users", HealthTrackerController::getAllUsers)
        app.get("/api/users/{user-id}", HealthTrackerController::getUserByUserId)
        app.post("/api/users", HealthTrackerController::addUser)
        app.get("/api/users/email/{email}", HealthTrackerController::getUserbyUserEmail)
        app.patch("/api/users/{user-id}", HealthTrackerController::updateUser)
        app.delete("/api/users/{user-id}", HealthTrackerController::deleteUser)

        //Activity endpoints
        app.get("/api/activities", HealthTrackerController::getAllActivities)
        app.post("/api/activities", HealthTrackerController::addActivity)
        app.get("/api/users/{user-id}/activities", HealthTrackerController::getActivitiesByUserId)
        app.delete("/api/users/{user-id}/activities", HealthTrackerController::deleteActivitiesByUserId) //Works
        app.delete("/api/activities/{activity-id}", HealthTrackerController::deleteActivity) //Works
        app.patch("/api/activities/{activity-id}", HealthTrackerController::updateActivity) //Works
        app.get("/api/activities/{activity-id}", HealthTrackerController::getActivityById) //Works
        //TODO: add endpoints from exercises here
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
