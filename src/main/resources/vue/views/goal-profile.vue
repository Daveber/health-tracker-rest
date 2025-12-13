<template id="goal-profile">
  <app-layout>
    <div>
      <form v-if="goal">
        <label class="col-form-label"> Goal ID: </label>
        <input class="form-control" v-model="goal.id" name="id" type="number" readonly/><br>

        <label class="col-form-label"> Target Calories: </label>
        <input class="form-control" v-model="goal.targetCalories" name="targetCalories" type="text"/><br>

        <label class="col-form-label"> User ID: </label>
        <input class="form-control" v-model="goal.userid" name="userid" type="number"/><br>

        <label class="col-form-label"> Recommended Activity ID: </label>
        <input class="form-control" v-model="goal.recommendedid" name="recommendedid" type="number"/><br>
      </form>
    </div>
  </app-layout>
</template>

<script>
app.component("goal-profile", {
  template: "#goal-profile",
  data:() => ({
    goal: null
  }),
  created: function () {
    const goalId = this.$javalin.pathParams["goal-id"];
    const url = `/api/goals/${goalId}`
    axios.get(url)
        .then(res => this.goal = res.data)
        .catch(() => alert("Error while fetching goal" + goalId));
  }
});
</script>