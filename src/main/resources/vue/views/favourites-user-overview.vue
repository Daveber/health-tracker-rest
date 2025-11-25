<template id="favourites-user-overview">
  <app-layout>
    <div>
      <h3>Favourites List</h3>
      <ul>
        <li v-for="favourite in favourites">
          {{favourite.id}}: {{favourite.activityid}} TODO: insert full activity here
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("favourites-user-overview",{
  template: "#favourites-user-overview",
  data: () => ({
    favourites: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/favourites`)
        .then(res => this.favourites = res.data)
        .catch(() => alert("Error while fetching favourites"));
  }
});
</script>