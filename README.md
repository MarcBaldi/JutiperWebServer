# Jutiper API
A server designed to be a persistent backend for Jutiper.

## Prerequisites
scala, sbt, Docker, mysql, wkhtmltox

## Built with 
Scala 2.11.0, sbt 1.2.8, MySQL 8.0.16

## Authors
* **Shohrukh Koyirov** - *Initial work* - [MisterShoh](https://github.com/MisterShoh)
* **Marc Baldischwieler** - *Initial work* - [MarcBaldi](https://github.com/MarcBaldi)

See also the list of [contributors](https://github.com/MarcBaldi/JutiperWebServer/graphs/contributors) who participated in this project.

## Acknowledgments
* Hat tip to anyone whose code was used
* Inspiration
* The HTWG and its Teamprojekt

## steps for developing:
- change source code
- push to master
- in VM: git pull
- restart container juti_api

dont restart the db-container mysql_jutiper, changes may be lost


## other stuff (maybe important)

If you have a local instance of mysql running (e.g. MySQL80), 
CMD as admin:
net stop MySQL80

uploading files to the VM
scp Dockerfile root@141.37.160.183:~

executing stuff in a docker container
docker exec -it juti /bin/bash

### Docker
steps done to achieve our docker set up

create a docker network:
docker network create jutiper-net

create mysql docker container and run it

docker run --name mysql_jutiper -e MYSQL_ROOT_PASSWORD=*insert_root_pw* -e MYSQL_USER=workbench -e MYSQL_PASSWORD=*insert_user_pw* -v /data/mysql:/var/lib/mysql -v /data/log:/var/log/mysql -p 3306:3306 --network jutiper-net -d mysql

create jutiper api docker container and run it

docker build -t juti_image . && docker run -it -d -p 8080:8080 --name juti_api --network jutiper-net juti_image 


## Final Infrastructure

### unity
simply run the 
Jutiper Prototyp Werkschau.exe from the build folder

### api
Running in Docker containers on the VM on the HTWG server.
Accessible within the HTWG VPN, by browser or the unity App. 

### db
Running in Docker containers on the VM on the HTWG server.
Accessible remotely within the HTWG VPN, with the mySQL workbench. 
