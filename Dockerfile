FROM openjdk:10
COPY . /home/marharyta/Документы/projects/clser2/server_gradle
WORKDIR /home/marharyta/Документы/projects/clser2/server_gradle
RUN apt-get update && apt-get install gradle \
&& gradle build \
&& cd build/libs
ENTRYPOINT ["java","-jar", "server-1.0.jar"]
