<template id="user-overview">
  <app-layout>

    <div class="card bg-light mb-3">
      <div class="card-header bg-primary text-black">
        <div class="row">
          <div class="col-6">
            Users
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
      <div class="card-body adduser" :class="{'d-none': hideForm}">
        <form id="addUser">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="formData.email" name="email" placeholder="Email"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link adduser-button" @click="addUser()">Add User</button>
      </div>
    </div>

    <div class="row">
      <div class="col-md-3 mb-4" v-for="(user,index) in users" :key="index">
        <div class="card text-center user-card">
          <h5 class="card-title">{{ user.name }}</h5>
          <div class="card-body">
            <p class="card-text">{{ user.email }}</p>
            <div class="d-flex justify-content-center">
              <a :href="`/users/${user.id}`" class="btn btn-update">
                <i class="fa fa-pencil"></i> Update
              </a>
              <button class="btn btn-delete" @click="deleteUser(user, index)">
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
app.component("user-overview", {
  template: "#user-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },

    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const userId = user.id;
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    addUser: function() {
      const url = `/api/users`;
      axios.post(url,
          {
            name: this.formData.name,
            email: this.formData.email
      })
          .then(response => {
            this.users.push(response.data)
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

.user-card {
  border: solid black;
  background: url('/users.jpg');
  background-position: bottom right;
}

.user-card .card-body {
  padding: 0.5rem;
}

.user-card .card-title {
  border-bottom: 0.5px solid black;
  background-color: #0affb3;
}

.user-card .card-header {
  font-size: 1rem;
  padding: 0.5rem;
}

.app-layout {
  background-image: url('/users.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}

.adduser {
  background: linear-gradient(180deg, #49adfb, springgreen);
}

.form-control {
  background: linear-gradient(135deg, #0affb3, #91fad7);
}

.adduser-button {
  background-color: dodgerblue;
  color: black;
  padding: 5px;
  font-size: small;
}

.input-group-text {
  background-color: springgreen;
  padding: 15px
}

</style>