FROM zgwmike/akka-sbt
WORKDIR /main
ADD . /main
RUN \
  curl -L -o wkhtml.deb https://downloads.wkhtmltopdf.org/0.12/0.12.5/wkhtmltox_0.12.5-1.stretch_amd64.deb && \
  apt-get update && \
  apt install -y ./wkhtml.deb && \
  rm wkhtml.deb
CMD sbt "run"