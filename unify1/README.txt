* Installera

** Mysql
** ServiceMix
** Eclipse
** Gradle (valfritt)

* Konfigurera

** Mysql
use test;
CREATE TABLE INVOICE (id int, time_occurred date, status_code int, sum int);

** ServiceMix
features:install camel-jdbc
${SERVICEMIX_CLIENT} "install -s mvn:mysql/mysql-connector-java/5.1.18"
osgi:install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-dbcp/1.4_3

Resultatet av ovanst책ende kommando 채r ett modul-id, byt fr책n 190 till det id:t i kommandot nedan
dev:dynamic-import 190

** Eclipse
P책 kommandoraden:
  Linux: ./gradlew eclipse
  Windows: gradlew eclipse

I Eclipse:
File -> Import... -> Existing Projects into Workspace

* Bygg och deploya

gradlew clean build
cp build/libs/unify1.jar <servicemix>/deploy

** K쉜a de vanliga testerna 
gradlew clean compile test // g똱 inte l둵gre - test 둹 inte unik l둵gre
gradlew clean build (omfattar ju s똩lart testfasen)

** K쉜a integrationstesterna
gradlew clean build integrationTest
- denna borde ut쉓as med l둴plig deploy f쉜st