FROM zgwmike/akka-sbt
WORKDIR /main
ADD . /main
CMD sbt "run"