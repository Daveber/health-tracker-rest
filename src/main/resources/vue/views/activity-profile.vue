<template id="activity-profile" xmlns="http://www.w3.org/1999/html">
  <app-layout>
    <div v-if="noActivityFound">
      <p> We're sorry, we were unable to retrieve this Activity.</p>
      <p> View <a :href="'/users'">all activities</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noActivityFound">

      <div class="card-header">
        <div class="row">
          <div class="col-6"> Activity Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateActivity()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteActivity()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>


      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-id">Activity ID</span>
            </div>
            <input type="number" class="form-control" v-model="activity.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="activity.description" name="description" placeholder="Description"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-duration">Duration</span>
            </div>
            <input type="number" class="form-control" v-model="activity.duration" name="duration" placeholder="Duration"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-calories">Calories</span>
            </div>
            <input type="number" class="form-control" v-model="activity.calories" name="calories" placeholder="Calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-started">Started</span>
            </div>
            <input type="number" class="form-control" v-model="activity.started" name="started" placeholder="Started"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-userid">UserID</span>
            </div>
            <input type="number" class="form-control" v-model="activity.userId" name="userid" placeholder="UserID"/>
          </div>
        </form>

      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null,
    noActivityFound: false,
    activities: []
  }),
  created: function () {
    const activityId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${activityId}`
    axios.get(url)
        .then(res => this.activity = res.data)
        .catch(error => {
          console.log("No activity found for the the current id in path parameter: " + error)
          this.noActivityFound = true
        })
  },

  methods: {
    updateActivity: function () {
      const activityId = this.$javalin.pathParams["activity-id"];
      const url = `/api/activities/${activityId}`
      axios.patch(url,
          {
            description: this.activity.description,
            duration: this.activity.duration,
            calories: this.activity.calories,
            started: this.activity.started,
            userId: this.activity.userId
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Activity updated!")
    },

    deleteActivity: function () {
      if (confirm("Do you really want to delete?")) {
        const activityId = this.$javalin.pathParams["activity-id"];
        const url = `/api/activities/${activityId}`
        axios.delete(url)
            .then(response => {
              alert("Activity deleted")
              window.location.href = '/activities';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>

<style>

.app-layout {
  background-image: url('/forest.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}
</style>