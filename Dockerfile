FROM clojure:alpine

RUN mkdir -p /usr/local/tea-alert
WORKDIR /usr/local/tea-alert
COPY project.clj /usr/local/tea-alert/
RUN lein deps

COPY . /usr/local/tea-alert
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" tea-alert-standalone.jar

CMD ["java", "-jar", "tea-alert-standalone.jar"]