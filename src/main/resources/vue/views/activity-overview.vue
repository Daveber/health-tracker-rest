<template id="activity-overview">
  <app-layout>

    <div class="card bg-light mb-3">
      <div class="card-header bg-primary text-black">
        <div class="row">
          <div class="col-6">
            Activities
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
        <form id="addActivity">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="Description" placeholder="Description"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activityid">Duration</span>
            </div>
            <input type="email" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-calories">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="formData.calories" name="Calories" placeholder="Calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-started">Started</span>
            </div>
            <input type="text" class="form-control" v-model="formData.started" name="Started" placeholder="Started"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-userid">UserID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="User ID" placeholder="User ID"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addActivity()">Add Activity</button>
      </div>
    </div>

    <div class="row">
      <div class="col-md-3 mb-4" v-for="(activity,index) in activities" :key="index">
        <div class="card text-center activity-card">
          <h5 class="card-title">{{ activity.description }}</h5>
          <div class="card-body">
            <p class="card-text">Duration: {{ activity.duration }}</p>
            <p class="card-text">Calories: {{ activity.calories }}</p>
            <div class="d-flex justify-content-center">
              <a :href="`/activities/${activity.id}`" class="btn btn-update">
                <i class="fa fa-pencil"></i> Update
              </a>
              <button class="btn btn-delete" @click="deleteActivity(activity, index)">
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
app.component("activity-overview", {
  template: "#activity-overview",
  data: () => ({
    activities: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchActivities();
  },
  methods: {
    fetchActivities: function () {
      axios.get("/api/activities")
          .then(res => this.activities = res.data)
          .catch(() => alert("Error while fetching Activities"));
    },

    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete this activity? This action cannot be undone.', 'Warning')) {
        //activity confirmed delete
        const activityId = activity.id;
        const url = `/api/activities/${activityId}`;
        axios.delete(url)
            .then(response =>
                //activities from the local state so Vue will reload list automatically
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    addActivity: function() {
      const url = `/api/activities`;
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: this.formData.started,
            userId: this.formData.userId
          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm=true;
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

.activity-card {
  border: solid black;
  background: url('/forest.jpg'), lightgray;
  background-blend-mode: overlay;
  background-position: center;
}

.activity-card .card-body {
  padding: 0.5rem;
}

.activity-card .card-title {
  border-bottom: 0.5px solid black;
  background-color: #0affb3;
}

.activity-card .card-header {
  font-size: 1rem;
  padding: 0.5rem;
}

.app-layout {
  background-image: url('/forest.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}

</style>