<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col-md-4">
        <div class="card text-center">
          <h5 class="card-header bg-primary text-black">
            <i class="fas fa-users"></i> Registered Users
          </h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}}</h5>
            <a href="/users" class="btn btn-outline-primary">View Users</a>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card text-center">
          <h5 class="card-header bg-primary text-black">
            <i class="fas fa-running"></i> Activities
          </h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}}</h5>
            <a href="/activities" class="btn btn-outline-primary">View Activities</a>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card text-center">
          <h5 class="card-header bg-primary text-black">
            <i class="fas fa-bullseye"></i> Goals
          </h5>
          <div class="card-body">
            <h5 class="card-title">{{goals.length}}</h5>
            <a href="/goals" class="btn btn-outline-primary">View Goals</a>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        goals: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching Users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching Activities"))
        axios.get("/api/goals")
            .then(res => this.goals = res.data)
            .catch(() => alert("Error while fetching Goals"))
      }
    });
</script>

<style>

.card {
  border: 1px solid black;
}

.card-body {
}

</style>