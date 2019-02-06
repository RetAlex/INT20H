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

    <button @click="getImages">getAll images</button>

<magic-grid class="container">

      <img :src="image" v-for="(image, i) in images" class="item"></img>
</magic-grid>
  </div>
</template>

<script>


export default {
  name: 'App',
  data() {
    return {
      images: []
    }
  },
  methods: {
    getImages() {

      var xmlhttp = new XMLHttpRequest();

      const url = '/api/getAllImages?albumId=72157674388093532&tag=int20h'

      xmlhttp.open("GET", url)
      xmlhttp.send()

      xmlhttp.onreadystatechange=(e)=> {
        console.log(xmlhttp.responseText)
      }

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
