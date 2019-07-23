FROM zgwmike/akka-sbt
WORKDIR /main
EXPOSE 8082
ADD . /main
CMD sbt "run"