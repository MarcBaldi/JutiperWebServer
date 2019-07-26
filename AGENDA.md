# TODO:

## API:

change email addresses of guest user to the most recent developers:

certificateController.scala -> sendCertificate -> change bcc value
AND
guest user in the database.

add interfaces for the layers

content of email (with the certificate)
more descriptive text.

content of pdf (the certificate) 
make it look nicer

docker exec -it juti /bin/bash docker exec -it juti /bin/bash 

correct password encryptyion. maybe use OAuth (login with google etc)

Grade calculation ( discuss again with design people) see Lessons/Exams

VersuchNr: 
/getUser/ should also return the VersuchNr (it must be read from the DB, there is a view: versuchnr_max_werte).
Also make Unity accept it (in api calls-> loginButton() ).

correct some german variable names into english

dynamic certificate:
HttpServer -> sendCertificate():
make the table list dynamic - i.e. ask in the DB which tables exist.

return missing answers:
HttpServer -> sendCertificate():
make it return all missing questions (will be displayed to the user)

protect project secrets: 
Mail.scala-> send -> gMail config
HttpServer-> connectDatabase -> DB root password


## UNITY:

feedback to user if server is not reachable

LessonController -> Init():
Investigate what is causing the lag and try to fix it.


### Lessons/Exams:

course 05 and 09: no exam

make all exams inherit from lesson controller and use its functions.(done for 8 of 10 lessons)

discuss with the designers how a fair marking system could be implemented


## DOCKER:
make the DB not loose updates if container is restarted



## helpful links:

stop local mysql instance: 
https://stackoverflow.com/questions/10885038/stop-mysql-service-windows

docker mysql image manual:
https://hub.docker.com/_/mysql

communication between containers:
https://docs.docker.com/network/bridge/

give access to remote users
(mysql workbench):
https://stackoverflow.com/questions/11922323/java-sql-sqlexception-access-denied-for-user-rootlocalhost-using-password

container used for scala environment:
https://hub.docker.com/r/zgwmike/akka-sbt

develop Docker containers in Intellij IDEA:
https://medium.com/beyond/running-a-play-app-with-docker-locally-and-debugging-with-intellij-idea-746eaf1732da

The Jutiper server ( only accessable in VPN)
http://141.37.160.183:8080/

Used software to make the certificate pdf:
https://wkhtmltopdf.org/index.html

Other links for testing:
http://141.37.160.183:8080/sendCertificate/%7B%22status%22:%22success%22,%22Username%22:%22Guest%22%7D

## restart

to restart the server, do the following:

login to the cmd as root ( e.g. via Putty),
run the DB with this command:

docker run --name mysql_jutiper -e MYSQL_ROOT_PASSWORD=jutiper2019 -e MYSQL_USER=workbench -e MYSQL_PASSWORD=workbench -v /data/mysql:/var/lib/mysql -v /data/log:/var/log/mysql -p 3306:3306 --network jutiper-net -d mysql

cd JutiperWebServer,
run the api with this command:

docker build -t juti_image . && docker run -it -d -p 8080:8080 --name juti_api --network jutiper-net juti_image

wait (this could take 5-10min)
