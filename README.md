Downlaod Latest Tag Release

Local Hosting:
for local hosting this should work out of the box when you run the project.

Openshift Hosting:
To host on openshift you need:
openshift url & openshift token in the "secrets and variables" section in settings.
<img width="968" height="130" alt="image" src="https://github.com/user-attachments/assets/0d524f66-6773-4fdb-a0cf-9e63cdc92fcd" />
These can be found by logging into openshift and in the top right corner clicking "show login command" both the url and token should be displayed.


File and function Descriptions:

Activity Controller:
Manages all HTTP endpoints related to user activties, handles JSON parsing, validations and response formatting.
1. getAllActivities: Retrieves all activitie
2. getActivitiesByUserId: Retrieves activites by user ID
3. addActivity: Creates activities
4. deleteActivitiesByUserId: Deletes activities with associated user ID
5. deleteActivity: Deletes a single activity by specified ID
6. updateActivity: updates an activity
7. getActivityById: retrieves a single activity by ID
(Further documentation and specific function descriptions can be found in comments and in generated documentation from actions)

Favourite Controller:
Manages all HTTP endpoints related to favourites, handles JSON parsing, validations and response formatting.
1. getAllFavourites: Retrieves all existing favourites
2. getFavouriteByFavouriteId: Retrieves a single favourite by specified ID
3. getFavouritesByUserId: Retrieves all favourites that has a specific userID
4. getFavouritesByActivityId: Retrieves all favourites by a specific activityID
