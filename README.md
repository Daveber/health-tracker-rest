Downlaod Latest Tag Release

OpenAPI documented in openapi_documentation.yaml file

Local Hosting:
for local hosting this should work out of the box when you run the project.

Openshift Hosting:
To host on openshift you need:

1. openshift url & openshift token in the "secrets and variables" section in settings.
<img width="968" height="130" alt="image" src="https://github.com/user-attachments/assets/0d524f66-6773-4fdb-a0cf-9e63cdc92fcd" />
These can be found by logging into openshift and in the top right corner clicking "show login command" both the url and token should be displayed.

2. Database tables corresponding to the files located in (ie/setu/domain/db/)
   These tables are going to required to correctly store data.



File and function Descriptions:

DBConfig
manages the database connection setup

DBUtilites
contains helper functions to convert database objects

JSONutilities
cotains helper functions for JSON serialization and deserialization.

JavalinConfig
configured and bootstraps the javalin web server, performs actiivties like static file handling, vue integration and global exception handling.
it also defined the routes for vue components and specific endpoints (favourites, goals, users, activities)


(controllers folder)
Activity Controller:
Manages all HTTP endpoints related to user activties, handles JSON parsing, validations and response formatting.
1. getAllActivities- Retrieves all activitie
2. getActivitiesByUserId- Retrieves activites by user ID
3. addActivity - Creates activities
4. deleteActivitiesByUserId - Deletes activities with associated user ID
5. deleteActivity - Deletes a single activity by specified ID
6. updateActivity - updates an activity
7. getActivityById - retrieves a single activity by ID
(Further documentation and specific function descriptions can be found in comments and in generated documentation from actions)



Favourite Controller:
Manages all HTTP endpoints related to favourites, handles JSON parsing, validations and response formatting.
1. getAllFavourites - Retrieves all existing favourites
2. getFavouriteByFavouriteId - Retrieves a single favourite by specified ID
3. getFavouritesByUserId - Retrieves all favourites that has a specific userID
4. getFavouritesByActivityId- Retrieves all favourites by a specific activityID
5. addFavourite - Adds a favourite
6. deleteFavouriteById - Delete favourite by iD
7. deleteFavouritesByUserId - Deletes all favourites with corresponding user iD
8. deleteFavouriteByActivityId - Deletes all favourites with corresponding actiivty ID
(Further documentation and specific function descriptions can be found in comments and in generated documentation from actions)



Goal Controller:
Manages all HTTP endpoints related to goals, handles JSON parsing, validations and response formatting.
1. getAllGoals - Retrieves all existing goals.
2. getGoalById - Retrieves a single goal by its goal ID.
3. getGoalByUserId - Retrieves the goal associated with a specific user ID.
4. addGoal – Adds a goal.
5. deleteGoal – Deletes a goal by ID.
6. updateGoal – Updates an existing goal using ID.
7. getRecommended – Returns a recommended activity based on a target caloretic value.
8. getRecommendedId – Returns a recommended activity based on a specific goal ID.
(Further documentation and specific function descriptions can be found in comments and in generated documentation from actions)



User Controller:
Manages all HTTP endpoints related to Users, handles JSON parsing, validations and response formatting.
1. getAllUsers – Retrieves all existing users
2. getUserByUserId – Retrieves a single user by user ID
3. getUserbyUserEmail – Retrieves a user by email address
4. addUser – Adds user
5. updateUser – Updates an existing user using user ID
6. deleteUser – Deletes a user by user ID


(repository foler)

ActivityDAO
Handles all database operations related to activities, including retrieval, creation, updating, and deletion.
getAll – Retrieves all activities.
1. findByActivityId – Finds a single activity by ID
2. findByUserId – Retrieves all activities associated with user iD
3. save – creates a new activity
4. deleteAllAssociatedByUserId – Deletes all activities associated with user id
5. deleteByActivityId – Deletes a single activity by activity id
6. update – Updates an activity



FavouriteDAO
Handles all database operations related to favourites, including retrieval, creation, updating, and deletion.
1. getAll – Retrieves all favourites
2. findByFavouriteId – Retrieves single favourite by id
3. findByUserId – Retrieves all favourites associated with a specific user ID
4. findByActivityId – Retrieves all favourites linked to a specific activity ID
5. save – creates a new favourite
6. deleteAllFavouritesByUserId – Deletes all favourites associated with user ID
7. deleteAllFavouritesByActivityId – Deletes all favourites associated with activity iD
8. deleteById – Deletes a single favourite by id



GoalDAO
Handles all database operations related to goals, including retrieval, creation, updating, and deletion.
1. getAll – Retrieves all goals
2. findByGoalId – Finds a single goal by its ID
3. findByUserId – Retrieves the goal associated with ID
4. save – creates goal
5. deleteById – Deletes a goal by ID
6. deleteByUserId – Deletes a goal associated with a specific ID
7. getRecommendation – Returns an activity recommendation based on a target calorie value. (1st implementation)
8. getRecommendationId – Returns the ID of the recommended activity based on target calories. (2nd implementation)
9. getRecommendationForGoal – Returns the recommended activity ID for a specific goal
10. update – Updates an existing goal



UserDAO
Handles all database operations related to users, including retrieval, creation, updating, and deletion.
1. getAll – Retrieves all users
2. findUserById – Finds a single user by user ID
3. findUserByEmail – Retrieves a user by email address
4. save – create a user
5. update – Updates an existing user
6. delete – Deletes a user by ID


(views folder)
This folder contains vues for specific endpoints that can be found in the JavalinConfig file.
