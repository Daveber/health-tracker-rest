<template id="favourite-overview">
  <app-layout>
    <div>
      <div>
        <ul class="favourite-overview-list">
          <li v-for="favourite in favourites">
            <a :href="`/favourites/${favourite.id}`"> {{favourite.userid}} ({{favourite.activityid}}) </a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("favourite-overview", {
  template: "#favourite-overview",
  data:() => ({
    favourites: [],
  }),
  created() {
    this.fetchFavourites();
  },
  methods: {
    fetchFavourites: function () {
      axios.get("/api/favourites")
          .then(res => this.favourites = res.data)
          .catch(() => alert("Error while fetching favourites"));
    }
  }
});
</script>