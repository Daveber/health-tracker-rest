<template id="favourite-profile">
  <app-layout>
    <div>
      <form v-if="favourite">
        <label class="col-form-label"> Favourite ID: </label>
        <input class="form-control" v-model="favourite.id" name="id" type="number" readonly/><br>

        <label class="col-form-label"> Activity ID TODO: input full activity here: </label>
        <input class="form-control" v-model="favourite.activityid" name="activityid" type="text"/><br>

        <label class="col-form-label"> User ID TODO: input full user here: </label>
        <input class="form-control" v-model="favourite.userid" name="userid" type="text"/><br>
      </form>
    </div>
  </app-layout>
</template>

<script>
app.component("favourite-profile", {
  template: "#favourite-profile",
  data:() => ({
    favourite: null
  }),
  created: function () {
    const favouriteId = this.$javalin.pathParams["favourite-id"];
    const url = `/api/favourites/${favouriteId}`
    axios.get(url)
        .then(res => this.favourite = res.data)
        .catch(() => alert("Error while fetching favourite" + favouriteId));
  }
});
</script>