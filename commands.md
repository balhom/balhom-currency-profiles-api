# Commands

* Project Creation

~~~
mvn io.quarkus.platform:quarkus-maven-plugin:3.19.1:create "-DprojectGroupId=org.balhom" "-DprojectArtifactId=balhom-currency-profiles-api" "-Dextensions=kotlin,rest-jackson"
~~~

* Install Yaml Config Reader

~~~
mvn quarkus:add-extension "-Dextensions=quarkus-config-yaml"
~~~

* Install Oidc

~~~
mvn quarkus:add-extension "-Dextensions=oidc"
~~~

* Install Mongo dependencies

~~~
mvn quarkus:add-extension "-Dextensions=io.quarkus:quarkus-mongodb-panache-kotlin"
~~~

* Install Kafka dependencies

~~~
mvn quarkus:add-extension "-Dextensions=messaging-kafka"
~~~

* Install S3 dependencies

~~~
mvn quarkus:add-extension "-Dextensions=amazon-s3"
~~~
