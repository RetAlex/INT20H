# INT20H
Test task for INT20H hackathon

Running via docker:
1) Build front-end part using command: **npm i && node run build**
2) Create docker container by running docker-compose build command
3) Run docker container via command: **docker-compose up [-d for background mode]**

To stop the service run command **docker-compose stop** or use **ctrl+c** combination if not in background mode
To destroy container run command docker-compose down

### Resources
By default application is running on port 8080. To translate it to another port change docker-compose.yml entry "expose" and change the first number to the required port.

### Known problems
?? Application possibly not using application*.properties files because of invalid resource folder configuration. Needs to be checked though