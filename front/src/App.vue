<template>
  <div id="app">

    <v-toolbar>

      <v-toolbar-title>Filters</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn
        small
        class="text-capitalize font-weight-medium"
        @click="getImages('/api/getAllImages?page=0')">
        All
      </v-btn>

      <v-btn
        small
        flat
        class="red accent-4 white--text text-capitalize font-weight-medium"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=anger&page=0')">
        Anger
      </v-btn>

      <v-btn
        small
        flat
        class="pink accent-4 white--text text-capitalize font-weight-medium"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=surprise&page=0')">
        Surprise
      </v-btn>

      <v-btn
        small
        flat
        class="yellow darken-4 white--text text-capitalize"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=happiness&page=0')">
        Happiness
      </v-btn>

      <v-btn
        small
        flat class="light-green darken-4 white--text text-capitalize font-weight-medium"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=disgust&page=0')">
        Disgust
      </v-btn>

      <v-btn
        small
        flat
        class="indigo white--text text-capitalize font-weight-medium">
        Saddness
      </v-btn>


      <v-btn
        small
        flat
        class="deep-purple accent-4 white--text text-capitalize font-weight-medium">
        Fear
      </v-btn>

      <v-btn
        small
        flat
        class="blue-grey darken-4 white--text text-capitalize font-weight-medium"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=neutral&page=0')">
        Neutral
      </v-btn>

    </v-toolbar>


    <masonry
      :cols="{default: 2, 1000: 2, 700: 2, 400: 1}"
      :gutter="{default: '30px', 700: '15px'}"
      class="container">

      <img :src="image.listOfSizes[7].source" v-for="(image, i) in images" :key="i" class="item">
    </masonry>

    <v-btn
      large
      class="white"
      v-if="link !== ''"
      @click="loadMore">
      Load more
    </v-btn>

  </div>
</template>

<script>


export default {
  name: 'App',
  data() {
    return {
      images: [],
      link: ''
    }
  },
  methods: {
    getImages(link) {
      this.link = link;

      let xml = new XMLHttpRequest();
      const url = `http://localhost:8079${link}`;

      xml.open("GET", url);
      xml.send();

      xml.onreadystatechange=(e)=> {
        if (xml.readyState === 4 && xml.status === 200) {

          this.images = JSON.parse(xml.response)
        }
      }
    },

    loadMore() {
      // console.log(this.link)
      let link = this.link.slice(0, -1);
      let page = parseInt(this.link[this.link.length - 1]);

      page++;

      let xml = new XMLHttpRequest();
      const url = `http://localhost:8079${link}${page}`;

      xml.open("GET", url);
      xml.send();

      xml.onreadystatechange=(e)=> {
        if (xml.readyState === 4 && xml.status === 200) {
          const newArr = JSON.parse(xml.response);

          for(let i = 0; i < newArr.length; i++) {
            this.images.push(newArr[i])
          }


        }
      }
    }

  },

  mounted() {
    this.getImages('/api/getAllImages?page=0')
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
