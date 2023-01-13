FROM clojure:openjdk-17-lein 
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN lein run
