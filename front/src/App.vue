<template>
  <div id="app">

    <v-toolbar>

      <v-toolbar-title>Filters</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn small flat class="red accent-4 white--text text-capitalize font-weight-medium">Anger</v-btn>
      <v-btn small flat class="pink accent-4 white--text text-capitalize font-weight-medium">Surprise</v-btn>
      <v-btn small flat class="yellow darken-4 white--text text-capitalize">Happiness</v-btn>

      <v-btn small flat class="light-green darken-4 white--text text-capitalize font-weight-medium">Disgust</v-btn>
      <v-btn small flat class="indigo white--text text-capitalize font-weight-medium">Saddness</v-btn>


      <v-btn small flat class="deep-purple accent-4 white--text text-capitalize font-weight-medium">Fear</v-btn>
      <v-btn small flat class="blue-grey darken-4 white--text text-capitalize font-weight-medium">Neutral</v-btn>
    </v-toolbar>


<magic-grid class="container">

      <div v-for="(post, i) in posts" class="item">{{post}}</div>
</magic-grid>
  </div>
</template>

<script>


export default {
  name: 'App',
  data() {
    return {
      posts: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.container {
  top: 1.25em;
}

.container div {
  width: 100%;
  height: 100px;
  background-color: cyan;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
}
.container .item:nth-child(2) { height: 200px; }
.container .item:nth-child(4n) { height: 800px; }
.container .item:nth-child(6) { height: 600px; }
.container .item:nth-child(11) { height: 400px; }

</style>
