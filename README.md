Downlaod Latest Tag Release

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

(db folder)
