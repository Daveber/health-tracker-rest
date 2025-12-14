<template id="app-layout">
  <div class="app-layout">
    <div class="container">
      <!-- Start of navbar -->
      <nav class="navbar navbar-expand-lg navbar-dark">
        <a class="navbar-brand fw-bold text-black" href="/">
          <i class="fas fa-home"></i>Home</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto">
            <li class="nav-item active">
              <a class="nav-link users-link" href="/users">
                <i class="fas fa-users"></i>Users</a>
            </li>
            <li class="nav-item">
              <a class="nav-link activities-link" href="/activities">
                <i class="fas fa-running"></i>Activities</a>
            </li>
            <li class="nav-item">
              <a class="nav-link favourites-link" href="/favourites">
                <i class="fas fa-heart"></i>Favourites</a>
            </li>
            <li class="nav-item">
              <a class="nav-link goal-link" href="/goals">
                <i class="fas fa-bullseye"></i>Goals</a>
            </li>
          </ul>
        </div>
      </nav>
      <!--End of nav bar-->
      <!--Start of main content area-->
      <div class="content mt-3">
        <div class="container-fluid">
          <slot></slot>
        </div>
      </div>
      <!--End of main content area-->
    </div>
  </div>
</template>

<script>
app.component("app-layout", {template: "#app-layout"});
</script>

<style>
.navbar {
  background-color: dodgerblue;
  border-radius: 5px;
}

.navbar .nav-link {
  padding: 0.75rem 1.25rem;
  margin-right: 0.5rem;
  border-radius: 5px;
}

.navbar .users-link:hover {
  color: #0affb3;
  background-color: #2d9eff;
}

.navbar .activities-link:hover {
  color: #00da00;
  background-color: #2d9eff;
}

.navbar .favourites-link:hover {
  color: red;
  background-color: #2d9eff;
}

.navbar .goal-link:hover {
  color: orangered;
  background-color: #2d9eff;
}

.navbar-brand {
  font-size: 1.5rem;
  padding-right: 1rem;
  padding-left: 1rem;
  letter-spacing: 0.5px;
}

.nav-item {
  fond-size: 1.0rem;
  letter-spacing: 0.5px;
}
</style>