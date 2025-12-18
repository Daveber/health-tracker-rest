<template id="user-profile" xmlns="http://www.w3.org/1999/html">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were unable to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">

      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>


      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
        </form>

        <div class="card-footer text-left-activities">
          <p v-if="activities.length === 0"> No activities yet...</p>
          <p v-if="activities.length > 0">  <i class="fas fa-running running-activity"></i> Activities <i class="fas fa-running running-activity"></i> </p>
          <ul>
            <div v-for="activity in activities">
              <i class="fas fa-running running-activities"></i> {{ activity.description }} for {{ activity.duration }} minutes <i class="fas fa-running running-activities"></i>
            </div>
          </ul>
        </div>

        <div class="card-footer text-left-favourites">
          <p v-if="activities.length === 0"> No Favourites yet...</p>
          <p v-if="activities.length > 0"> <i class="fas fa-heart favourite-activity"></i> Favourites <i class="fas fa-heart favourite-activity"></i> </p>
          <ul>
            <div v-for="activity in favourites">
              <i class="fas fa-heart favourite-activities"></i> {{activity.description}} <i class="fas fa-heart favourite-activities"></i>
            </div>
          </ul>
        </div>

        <div class="card-footer text-left-goals">
          <p v-if="!goal"> No Goal</p>
          <p v-else > <i class="fas fa-bullseye goal-bullseye"></i> Goal <i class="fas fa-bullseye goal-bullseye"></i> </p>
          <div v-if="goal">
              <i class="fas fa-bullseye goals-bullseye">
              </i> Burn {{goal.targetCalories}} Calories
              <i class="fas fa-bullseye goals-bullseye"></i>
          </div>
        </div>

      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    favourites: [],
    goal: null,
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for the the current id in path parameter: " + error)
          this.noUserFound = true
        })

    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet: " + error)
          this.activities = [];
        })

    this.getUserfavourites();
    this.getUsergoals();
  },

  methods: {
    updateUser: function() {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
            name: this.user.name,
            email: this.user.email
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },

    deleteUser: function() {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    async getUserfavourites() {
      const userId = this.$javalin.pathParams["user-id"];
      try {
        const favourites = await axios.get(`/api/users/${userId}/favourites`);
        const favouriteData = favourites.data;

        const activities = favouriteData.map(fav => axios.get(`/api/activities/${fav.activityid}`));

        const responses = await Promise.all(activities);

        this.favourites = responses.map(response => response.data);
      } catch (error) {
        this.favourites = [];
        console.log("User has no favourites.." + error)
      }
    },

    async getUsergoals() {
      const userId = this.$javalin.pathParams["user-id"];
      try {
        const response = await axios.get(`/api/users/${userId}/goals`);
        this.goal = response.data
      } catch (error) {
        this.goal = null;
        console.log("User has no goals")
      }
    },

  }
});
</script>

<style>

.app-layout {
  background-image: url('/users.jpg');
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

.text-left-activities {
  border: 1px solid gray;
  background: linear-gradient(135deg, #1e90ff, #63b3ed);
  padding: 15px;
  margin-top: 15px;
  color: black;
}

.text-left-favourites {
  border: 1px solid gray;
  background: linear-gradient(135deg, #1e90ff, #63b3ed);
  padding: 15px;
  margin-top: 15px;
  color: black
}

.input-group-text {
  border: 1px solid black;
  background-color: springgreen;
}

.form-control {
  border:  1px solid black;
  background: linear-gradient(135deg, #0affb3, #91fad7);
}

.running-activity {
  color: #0AFFB3FF;
  font-size: x-large;
}

.favourite-activity {
  color: red;
  font-size: large;
}

.running-activities {
  color: #0affb3;
  font-size: medium;
}

.favourite-activities {
  color: red;
  font-size: medium;
}

.text-left-goals {
  border: 1px solid gray;
  background: linear-gradient(135deg, #1e90ff, #63b3ed);
  padding: 15px;
  margin-top: 15px;
  color: black;
}
</style>