<template id="activity-profile">
  <app-layout>
    <div>
      <form v-if="activity">
        <label class="col-form-label"> Activity ID: </label>
        <input class="form-control" v-model="activity.id" name="id" type="number" readonly/><br>

        <label class="col-form-label"> Description: </label>
        <input class="form-control" v-model="activity.description" name="activity name" type="text"/><br>

        <label class="col-form-label"> Duration: </label>
        <input class="form-control" v-model="activity.duration" name="email" type="email"/><br>

        <label class="col-form-label"> Calories: </label>
        <input class="form-control" v-model="activity.calories" name="email" type="email"/><br>

        <label class="col-form-label"> Started: </label>
        <input class="form-control" v-model="activity.started" name="email" type="email"/><br>

        <label class="col-form-label"> UserID: </label>
        <input class="form-control" v-model="activity.userId" name="email" type="email"/><br>
      </form>
    </div>
  </app-layout>
</template>

<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data:() => ({
    activity: null
  }),
  created: function () {
    const activityId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${activityId}`
    axios.get(url)
        .then(res => this.activity = res.data)
        .catch(() => alert("Error while fetching activity" + activityId));
  }
});
</script>