<template>
  <div id="app">

    <div class="loader" v-if="loader">
      <div class="breeding-rhombus-spinner">
        <div class="rhombus child-1"></div>
        <div class="rhombus child-2"></div>
        <div class="rhombus child-3"></div>
        <div class="rhombus child-4"></div>
        <div class="rhombus child-5"></div>
        <div class="rhombus child-6"></div>
        <div class="rhombus child-7"></div>
        <div class="rhombus child-8"></div>
        <div class="rhombus big"></div>
      </div>
    </div>

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
        class="indigo white--text text-capitalize font-weight-medium"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=sadness&page=0')">
        Saddness
      </v-btn>


      <v-btn
        small
        flat
        class="deep-purple accent-4 white--text text-capitalize font-weight-medium"
        @click="getImages('/api/getListOfSizesByEmotion?emotion=fear&page=0')">
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


    <!--<masonry-->
      <!--:cols="{default: 2, 1000: 2, 700: 2, 400: 1}"-->
      <!--:gutter="{default: '30px', 700: '15px'}"-->
      <!--class="container">-->

      <!--<img :src="image.listOfSizes[7].source" v-for="(image, i) in images" :key="i" class="item">-->
    <!--</masonry>-->

    <stack :column-min-width="320" :gutter-width="8" :gutter-height="8" monitor-images-loaded>
      <stack-item v-for="(image, i) in images" :key="i" style="transition: left 300ms, top 300ms">
        <img :src="image.listOfSizes[5].source"/>
      </stack-item>
    </stack>

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
  import { Stack, StackItem } from 'vue-stack-grid';

export default {
  name: 'App',
  data() {
    return {
      loader: true,
      images: [],
      link: '',
      pageCounter: 0
    }
  },
  components: { Stack, StackItem },
  methods: {
    getImages(link) {

      this.loader = true;
      this.link = link;

      let xml = new XMLHttpRequest();
      const url = `${link}`;

      xml.open("GET", url);
      xml.send();

      xml.onreadystatechange=(e)=> {
        if (xml.readyState === 4 && xml.status === 200) {

          this.images = JSON.parse(xml.response).photoSizeDtos

          this.loader = false;
        }
      }

      this.pageCounter = parseInt(this.link[this.link.length - 1]);
    },

    loadMore() {
      this.loader = true;
      let link = this.link.slice(0, -1);

      this.pageCounter = this.pageCounter + 1;

      let xml = new XMLHttpRequest();
      const url = `${link}${this.pageCounter}`;

      xml.open("GET", url);
      xml.send();

      xml.onreadystatechange=(e)=> {
        if (xml.readyState === 4 && xml.status === 200) {
          const newArr = JSON.parse(xml.response).photoSizeDtos;

          for(let i = 0; i < newArr.length; i++) {
            this.images.push(newArr[i])
          }

          this.loader = false;
        }
      }

    }

  },

  mounted() {
    this.getImages('/api/getAllImages?page=0');
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

img {
  width: 100%;
  height: 100%;
}

.v-lazy-image {
  filter: blur(10px);
  transition: filter 0.7s;
}

.v-lazy-image-loaded {
  filter: blur(0);
}

.loader {
  z-index: 1;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  position: fixed;

  display: flex;
  align-items: center;
  justify-content: center;

  background-color: rgba(255, 255, 255);
}

.breeding-rhombus-spinner {
  height: 65px;
  width: 65px;
  position: relative;
  transform: rotate(45deg);
}

.breeding-rhombus-spinner, .breeding-rhombus-spinner * {
  box-sizing: border-box;
}

.breeding-rhombus-spinner .rhombus {
  height: calc(65px / 7.5);
  width: calc(65px / 7.5);
  animation-duration: 2000ms;
  top: calc(65px / 2.3077);
  left: calc(65px / 2.3077);
  background-color: #ff1d5e;
  position: absolute;
  animation-iteration-count: infinite;
}

.breeding-rhombus-spinner .rhombus:nth-child(2n+0) {
  margin-right: 0;
}

.breeding-rhombus-spinner .rhombus.child-1 {
  animation-name: breeding-rhombus-spinner-animation-child-1;
  animation-delay: calc(100ms * 1);
}

.breeding-rhombus-spinner .rhombus.child-2 {
  animation-name: breeding-rhombus-spinner-animation-child-2;
  animation-delay: calc(100ms * 2);
}

.breeding-rhombus-spinner .rhombus.child-3 {
  animation-name: breeding-rhombus-spinner-animation-child-3;
  animation-delay: calc(100ms * 3);
}

.breeding-rhombus-spinner .rhombus.child-4 {
  animation-name: breeding-rhombus-spinner-animation-child-4;
  animation-delay: calc(100ms * 4);
}

.breeding-rhombus-spinner .rhombus.child-5 {
  animation-name: breeding-rhombus-spinner-animation-child-5;
  animation-delay: calc(100ms * 5);
}

.breeding-rhombus-spinner .rhombus.child-6 {
  animation-name: breeding-rhombus-spinner-animation-child-6;
  animation-delay: calc(100ms * 6);
}

.breeding-rhombus-spinner .rhombus.child-7 {
  animation-name: breeding-rhombus-spinner-animation-child-7;
  animation-delay: calc(100ms * 7);
}

.breeding-rhombus-spinner .rhombus.child-8 {
  animation-name: breeding-rhombus-spinner-animation-child-8;
  animation-delay: calc(100ms * 8);
}

.breeding-rhombus-spinner .rhombus.big {
  height: calc(65px / 3);
  width: calc(65px / 3);
  animation-duration: 2000ms;
  top: calc(65px / 3);
  left: calc(65px / 3);
  background-color: #ff1d5e;
  animation: breeding-rhombus-spinner-animation-child-big 2s infinite;
  animation-delay: 0.5s;
}


@keyframes breeding-rhombus-spinner-animation-child-1 {
  50% {
    transform: translate(-325%, -325%);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-2 {
  50% {
    transform: translate(0, -325%);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-3 {
  50% {
    transform: translate(325%, -325%);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-4 {
  50% {
    transform: translate(325%, 0);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-5 {
  50% {
    transform: translate(325%, 325%);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-6 {
  50% {
    transform: translate(0, 325%);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-7 {
  50% {
    transform: translate(-325%, 325%);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-8 {
  50% {
    transform: translate(-325%, 0);
  }
}

@keyframes breeding-rhombus-spinner-animation-child-big {
  50% {
    transform: scale(0.5);
  }
}


</style>
