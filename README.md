Jutiper readme file

IF you have a local instance of mysql running (e.g. MySQL80), 
as admin:
net stop MySQL80

# docker
steps done to achieve our docker set up

create mysql docker container
and run it

docker run --name mysql_jutiper -e MYSQL_ROOT_PASSWORD=jutiper2019 -e MYSQL_USER=workbench -e MYSQL_PASSWORD=workbench -v /data/mysql:/var/lib/mysql -v /data/log:/var/log/mysql -p 3306:3306 --network jutiper-net -d mysql

create jutiper api docker container
and run it

docker build -t juti_image . && docker run -it -d -p 8080:8080 --name juti_api --network jutiper-net juti_image 

create a docker network:
docker network create jutiper-net

IF not already done via run command: 
connect the two containers to the same network:
docker network connect jutiper-net mysql  
docker network connect jutiper-net juti_api  

uploading files to the VM
scp Dockerfile root@141.37.160.183:~

executing stuff in a docker container
docker exec -it juti /bin/bash  
