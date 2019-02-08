<template>
  <div id="app">

    <v-toolbar>

      <v-toolbar-title>Filters</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn small class="text-capitalize font-weight-medium" @click="getImages('/api/getAllImages?albumId=72157674388093532&tag=int20h')">All</v-btn>
      <v-btn small flat class="red accent-4 white--text text-capitalize font-weight-medium">Anger</v-btn>
      <v-btn small flat class="pink accent-4 white--text text-capitalize font-weight-medium">Surprise</v-btn>
      <v-btn small flat class="yellow darken-4 white--text text-capitalize">Happiness</v-btn>

      <v-btn small flat class="light-green darken-4 white--text text-capitalize font-weight-medium">Disgust</v-btn>
      <v-btn small flat class="indigo white--text text-capitalize font-weight-medium">Saddness</v-btn>


      <v-btn small flat class="deep-purple accent-4 white--text text-capitalize font-weight-medium">Fear</v-btn>
      <v-btn small flat class="blue-grey darken-4 white--text text-capitalize font-weight-medium" @click="getImages('/api/getListOfSizesByEmogy?emogy=neutral')">Neutral</v-btn>
    </v-toolbar>


  <!--<div class="">-->


  <!--</div>-->
    <masonry
      :cols="{default: 4, 1000: 3, 700: 2, 400: 1}"
      :gutter="{default: '30px', 700: '15px'}"
      class="container"
    >
      <!--<div v-for="(item, index) in items" :key="index">Item: {{index + 1}}</div>-->
      <img :src="image[5].source" v-for="(image, i) in images" :key="i" class="item"/>
    </masonry>

    <!--<span v-for="(image, i) in images" class="items">{{image[5].source}} {{i}}</span>-->
  </div>
</template>

<script>


export default {
  name: 'App',
  data() {
    return {
      images: [
      ]
    }
  },
  methods: {
    getImages(link) {

      let xmlhttp = new XMLHttpRequest();
      const url = `http://localhost:8079${link}`;

      xmlhttp.open("GET", url)
      xmlhttp.send()

      xmlhttp.onreadystatechange=(e)=> {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

          this.images = JSON.parse(xmlhttp.response)
        }
        // this.images = xmlhttp.responseXML
      }

    },



  },

  mounted() {
    // this.getImages()
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
  /*top: 1.25em;*/
  max-width: 100%;
}

/*.container div {*/
  /*width: 100%;*/
  /*height: 100px;*/
  /*background-color: cyan;*/
  /*display: flex;*/
  /*justify-content: center;*/
  /*align-items: center;*/
  /*border-radius: 8px;*/
/*}*/

.item{
  /*display: block;*/
  width: 100%;
  /*height: 100%;*/
  margin: 20px;
  object-fit: contain;
}
</style>
