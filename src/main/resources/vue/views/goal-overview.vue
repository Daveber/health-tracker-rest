<template id="goal-overview">
  <app-layout>
    <div>
      <div>
        <ul class="goals-overview-list">
          <li v-for="goal in goals">
            <a :href="`/goals/${goal.id}`"> {{goal.id}} ({{goal.targetCalories}}) </a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("goal-overview", {
  template: "#goal-overview",
  data:() => ({
    goals: [],
  }),
  created() {
    this.fetchGoals();
  },
  methods: {
    fetchGoals: function () {
      axios.get("/api/goals")
          .then(res => this.goals = res.data)
          .catch(() => alert("Error while fetching Goals"));
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
</style>