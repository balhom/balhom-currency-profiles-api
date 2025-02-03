# Commands

* Project Creation

~~~
mvn io.quarkus.platform:quarkus-maven-plugin:3.17.8:create -DprojectGroupId=org.balhom -DprojectArtifactId=balhom-currency-profiles-api -Dextensions='kotlin,rest-jackson'
~~~

* Install Yaml Config Reader

~~~
mvn quarkus:add-extension "-Dextensions=quarkus-config-yaml"
~~~

* Install Oidc

~~~
mvn quarkus:add-extension "-Dextensions=oidc"
~~~

* Install Vault

~~~
mvn quarkus:add-extension "-Dextensions=vault"
~~~

* Install Vault

~~~
mvn quarkus:add-extension "-Dextensions=io.quarkus:quarkus-mongodb-panache-kotlin"
~~~
