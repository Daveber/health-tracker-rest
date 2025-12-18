<template id="favourite-overview">
  <app-layout>

    <div class="row">
    <div class="col-md-3 mb-4" v-for="(favourite,index) in favourites" :key="index">
      <div class="card text-center favourite-card">
        <h5 class="card-title">{{ users[index]?.name }}</h5>
        <div class="card-body">
          <p class="card-text"> Activity: {{ activities[index]?.description }}</p>
          <button class="btn btn-delete" @click="deleteFavourite(favourite, index)">
            <i class="fas fa-heart-broken unfavourite"></i>
          </button>
          <div class="d-flex justify-content-center">
          </div>
        </div>
      </div>
    </div>
    </div>
  </app-layout>
</template>

<script>
app.component("favourite-overview", {
  template: "#favourite-overview",
  data:() => ({
    favourites: [],
    users: [],
    activities: [],
    activity: null
  }),
  created() {
    this.fetchFavourites();
  },
  methods: {
    async fetchFavourites() {
      try {

        const res = await axios.get(`/api/favourites`);
        this.favourites = res.data;

        this.users = [];
        this.activities = [];

        for (const favourite of this.favourites) {
          await this.getUser(favourite.userid);
          await this.getActivity(favourite.activityid);
        }
      } catch (error) {
        alert("Favourites not found");
      }
    },

    async getUser(userID) {
      try {
        const user = await axios.get(`/api/users/${userID}`);
        this.users.push(user.data)
      } catch (error) {
        console.log("User name not found" + error)
      }
    },

    async getActivity(activityID) {
      try {
        const activity = await axios.get(`/api/activities/${activityID}`);
        this.activities.push(activity.data)
      } catch (error) {
        console.log("Activity name not found" + error)
      }
    },

    deleteFavourite: function (favourite, index) {
      if (confirm('Is this activity no longer you favourite? :( ')) {
        const favouriteID = favourite.id;
        const url = `/api/favourites/${favouriteID}`
        axios.delete(url)
            .then(response => this.favourites.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log("Failed to unfavourite")
            });
      }
    }
  }
});
</script>

<style>
.favourite-card {
  border: solid black;
  background: url('/favourites1.jpg');
  background-position: bottom right;
}

.favourite-card .card-body {
  padding: 0.5rem;
}

.favourite-card .card-title {
  border-bottom: 0.5px solid black;
  background-color: #ff0a0a;
}

.favourite-card .card-header {
  font-size: 1rem;
  padding: 0.5rem;
}

.app-layout {
  background-image: url('/favourites1.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}

.card-text {
  font-size: large;
}

.unfavourite {
  font-size: large;
  color: blue;
}

.btn-delete:hover {
  background-color: gray;
}
</style>