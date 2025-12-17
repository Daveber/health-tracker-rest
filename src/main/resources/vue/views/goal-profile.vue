<template id="goal-profile">
<app-layout>
    <div v-if="noGoalFound">
      <p> We're sorry, we were unable to retrieve this Goal.</p>
      <p> View <a :href="'/goals'">all goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noGoalFound">

      <div class="card-header">
        <div class="row">
          <div class="col-6"> Goal Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateGoal()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteGoal()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-goal-id">Goal ID</span>
            </div>
            <input type="number" class="form-control" v-model="goal.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="goal.userid" name="userid" placeholder="UserID"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Target Calories</span>
            </div>
            <input type="number" class="form-control" v-model="goal.targetCalories" name="targetCalories" placeholder="Calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Recommended ID</span>
            </div>
            <input type="number" class="form-control" v-model="goal.recommendedid" name="recommendedid" placeholder="Recommended Activity ID"/>
          </div>
        </form>

        <div v-if="recommendedActivity" class="recommended-activity">
          <div class= "Recommended-Title">
          <h5><i class="fas fa-check-circle" style="color:rgba(1,188,1,0.75);"></i>
                Recommended Activity
              <i class="fas fa-check-circle" style="color:rgba(1,188,1,0.75)"></i>
          </h5>
          </div>
          <p> Description: {{ recommendedActivity.description }} <br>
            <small class="text-muted">Builds endurance <i class="fas fa-check" style="color:#00ff00"></i></small>
          </p>
          <p> Calories: {{ recommendedActivity.targetCalories }} <br>
            <small class="text-muted">Best match for calorie goal <i class="fas fa-check" style="color:#00ff00"></i></small>
            <div class="recommended-activity-id">ID: {{ recommendedActivity.id}} </div>
          </p>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("goal-profile", {
  template: "#goal-profile",
  data: () => ({
    goal: null,
    noGoalFound: false,
    goals: [],
    recommendedActivity: null
  }),
  created: function () {
    const goalId = this.$javalin.pathParams["goal-id"];
    const url = `/api/goals/${goalId}`
    axios.get(url)
        .then(res => {
          this.goal = res.data;
          this.getRecommendedActivity();
        })
        .catch(error => {
          console.log("No goal found for the the current id in path parameter: " + error)
          this.noGoalFound = true
        })
  },

  methods: {
    updateGoal: function () {
      const goalId = this.$javalin.pathParams["goal-id"];
      const url = `/api/goals/${goalId}`
      axios.patch(url,
          {
            userid: this.goal.userid,
            targetCalories: this.goal.targetCalories,
            recommendedid: this.goal.recommendedid
          })
          .then(response =>
              this.goal.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Goal updated!")
    },

    deleteGoal: function () {
      if (confirm("Do you really want to delete?")) {
        const goalId = this.$javalin.pathParams["goal-id"];
        const url = `/api/goals/${goalId}`
        axios.delete(url)
            .then(response => {
              alert("Goal deleted")
              window.location.href = '/goals';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    async getRecommendedActivity () {
      try {
        const goalId = this.$javalin.pathParams["goal-id"];
        const recommendedResponse = await axios.get(`/api/goals/${goalId}/recommended`);
        const recommendedID = recommendedResponse.data;

        if (recommendedID) {
        }
        const activityResponse = await axios.get(`/api/activities/${recommendedID}`);
        this.recommendedActivity = activityResponse.data;
      } catch(error) {
        console.log(error)
      }
    }
  }
});

</script>

<style>

.app-layout {
  background-image: url('/goals.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}

.card-header {
  background-color: dodgerblue;
  color: black;
  font-weight: bold; font-size: 1.2rem;
}


.card-body {
  background-color: #6fc2f3;
  color: black;
  font-weight: normal; font-size: 1.1rem;

}

.form-control {
  border: 1px solid #b3d7ff;
  color: #004085;
}

.fa-trash {
  color: black;
}

.fa-save {
  color: black;
}

.input-group-text {
  border: 1px solid black;
  background-color: springgreen;
}

.form-control {
  border:  1px solid black;
  background: linear-gradient(135deg, #0affb3, #91fad7);
}

.recommended-activity {
  background-color: #0a83fb;
  border: 1px solid lightgray;
  border-radius: 12px;
  padding: 12px;
  max-height: 180px;
}

.Recommended-Title {
  text-align: center;
}

.recommended-activity-id {
  text-align: end;
  margin-top: -32px;
}
</style>