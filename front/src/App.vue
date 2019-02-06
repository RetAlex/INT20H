<template>
  <div id="app">

    <v-toolbar>

      <v-toolbar-title>Filters</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn small  class="text-capitalize font-weight-medium">All</v-btn>
      <v-btn small flat class="red accent-4 white--text text-capitalize font-weight-medium">Anger</v-btn>
      <v-btn small flat class="pink accent-4 white--text text-capitalize font-weight-medium">Surprise</v-btn>
      <v-btn small flat class="yellow darken-4 white--text text-capitalize">Happiness</v-btn>

      <v-btn small flat class="light-green darken-4 white--text text-capitalize font-weight-medium">Disgust</v-btn>
      <v-btn small flat class="indigo white--text text-capitalize font-weight-medium">Saddness</v-btn>


      <v-btn small flat class="deep-purple accent-4 white--text text-capitalize font-weight-medium">Fear</v-btn>
      <v-btn small flat class="blue-grey darken-4 white--text text-capitalize font-weight-medium">Neutral</v-btn>
    </v-toolbar>

    <!--<button @click="getImages">getAll images</button>-->

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
      images: ["https://farm6.static.flickr.com/5747/30323171370_4fe6acfdd0_o.jpg",
        "https://farm6.static.flickr.com/5701/30505809682_fff88660ea_o.jpg",
        "https://farm9.static.flickr.com/8669/30586502076_3591895aa2_o.jpg",
        "https://farm6.static.flickr.com/5751/30622601835_e10cbdd17b_o.jpg",
        "https://farm6.static.flickr.com/5325/30534569101_89fdcd9e66_o.jpg",
        "https://farm6.static.flickr.com/5823/30505817432_dc6daede2e_o.jpg",
        "https://farm6.static.flickr.com/5348/30322727140_1dc97a6618_o.jpg",
        "https://farm6.static.flickr.com/5695/29988859413_709b1fa93c_o.jpg",
        "https://farm6.static.flickr.com/5775/30534579251_4cf2bb25ee_o.jpg",
        "https://farm6.static.flickr.com/5687/30586521486_a4f77d10d5_o.jpg",
        "https://farm6.static.flickr.com/5516/30534584281_4165b24558_o.jpg",
        "https://farm6.static.flickr.com/5632/30534586291_d85b75b3d8_o.jpg",
        "https://farm6.static.flickr.com/5644/29988864813_9afdaac302_o.jpg",
        "https://farm6.static.flickr.com/5659/30622614725_945a6afd75_o.jpg",
        "https://farm6.static.flickr.com/5741/30534593091_c4c0e338dd_o.jpg",
        "https://farm6.static.flickr.com/5580/29988870183_90dd648ed2_o.jpg",
        "https://farm6.static.flickr.com/5669/30322744930_5d0f77c85e_o.jpg",
        "https://farm6.static.flickr.com/5473/30322748840_146bd6594a_o.jpg",
        "https://farm6.static.flickr.com/5673/30622625205_79bac9544d_o.jpg",
        "https://farm6.static.flickr.com/5346/30622631115_b40965de93_o.jpg"
      ]
    }
  },
  methods: {
    getImages() {

      var xmlhttp = new XMLHttpRequest();

      const url = 'http://localhost:8079/api/getAllImages?albumId=72157674388093532&tag=int20h'

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


</style>
