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

        <div class="card-footer text-left">
          <p v-if="activities.length === 0"> No activities yet...</p>
          <p v-if="activities.length > 0"> Activities so far...</p>
          <ul>
            <li v-for="activity in activities">
              {{ activity.description }} for {{ activity.duration }} minutes
            </li>
          </ul>
        </div>

        <div class="card-footer text-left">
          <p v-if="activities.length === 0"> No Favourites yet...</p>
          <p v-if="activities.length > 0"> Favourites so far...</p>
          <ul>
            <li v-for="activity in favourites">
              Activity id: {{ activity.id }} Activity Name: {{activity.description}} TODO: Add Heart icon
            </li>
          </ul>
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
        })

    this.getUserfavourites();
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
        console.log("User has no favourites.." + error)
      }
    }
  }
});
</script>