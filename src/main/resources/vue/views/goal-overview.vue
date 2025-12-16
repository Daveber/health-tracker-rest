<template id="goal-overview">
<!--  <app-layout>-->
<!--    <div>-->
<!--      <div>-->
<!--        <ul class="goals-overview-list">-->
<!--          <li v-for="goal in goals">-->
<!--            <a :href="`/goals/${goal.id}`"> {{goal.id}} ({{goal.targetCalories}}) </a>-->
<!--          </li>-->
<!--        </ul>-->
<!--      </div>-->
<!--    </div>-->
<!--  </app-layout>-->

  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header bg-primary text-black">
        <div class="row">
          <div class="col-6">
            Goals
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm=!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{'d-none': hideForm}">
        <form id="addGoal">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-goal-userid">GoalUserID</span>
            </div>
            <input type="number" class="form-control" v-model="formData.goaluserid" name="goaluserid" placeholder="GoalUserID"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-calorie-gaol">Calories</span>
            </div>
            <input type="number" class="form-control" v-model="formData.goaltargetCalories" name="goaltargetCalories" placeholder="TargetCalories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-goal-recommended-idl">GoalRecommendedID</span>
            </div>
            <input type="number" class="form-control" v-model="formData.goalrecommendedid" name="goalrecommendedid" placeholder="RecommendedID"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addGoal()">Add Goal</button>
      </div>
    </div>

    <div class="row">
      <div class="col-md-3 mb-4" v-for="(goal,index) in goals" :key="index">
        <div class="card text-center goal-card">
          <h5 class="card-title">{{ goal.userid }}</h5>
          <div class="card-body">
            <p class="card-text">{{ goal.recommendedid }}</p>
            <div class="d-flex justify-content-center">
              <a :href="`/goals/${goal.id}`" class="btn btn-update">
                <i class="fa fa-pencil"></i> Update
              </a>
              <button class="btn btn-delete" @click="deleteGoal(goal, index)">
                <i class="fas fa-trash"></i> Delete
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("goal-overview", {
  template: "#goal-overview",
  data:() => ({
    goals: [],
    formData: [],
    hideForm:true,
  }),
  created() {
    this.fetchGoals();
  },
  methods: {
    fetchGoals: function () {
      axios.get("/api/goals")
          .then(res => this.goals = res.data)
          .catch(() => alert("Error while fetching Goals"));
    },

    deleteGoal: function (goal, index) {
      if (confirm('Are you sure you want to delete this goal? This action cannot be undone.', 'Warning')) {
        //goal confirmed delete
        const goalId = goal.id;
        const url = `/api/goals/${goalId}`;
        axios.delete(url)
            .then(response =>
                this.goals.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    addGoal: function () {
      const url = `/api/goals`;
      axios.post(url,
          {
            userid: this.formData.goaluserid,
            targetCalories: this.formData.goaltargetCalories,
            recommendedid: this.formData.goalrecommendedid
          })
          .then(response => {
            this.goals.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>

<style>

.btn {
  font-size: x-small;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  border: 1px solid;
  background-color: transparent;
  margin: 0 0.25rem;
}

.btn-update:hover {
  background-color: #49adfb;
}


.btn-delete:hover {
  background-color: red;
}

.fa-plus {
  color: white;
}

.goal-card {
  border: solid black;
  background: url('/goals.jpg'), lightgray;
  background-blend-mode: overlay;
  background-size: contain;
}

.goal-card .card-body {
  padding: 0.5rem;
}

.goal-card .card-title {
  border-bottom: 0.5px solid black;
  background-color: #ff5920;
}

.goal-card .card-header {
  font-size: 1rem;
  padding: 0.5rem;
}

.app-layout {
  background-image: url('/goals.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}
</style>