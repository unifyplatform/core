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

Resultatet av ovanstående kommando är ett modul-id, byt från 190 till det id:t i kommandot nedan
dev:dynamic-import 190

** Eclipse
På kommandoraden:
  Linux: ./gradlew eclipse
  Windows: gradlew eclipse

I Eclipse:
File -> Import... -> Existing Projects into Workspace

* Bygg och deploya

gradlew clean build
cp build/libs/unify1.jar <servicemix>/deploy

** K�ra de vanliga testerna 
gradlew clean compile test // g�r inte l�ngre - test �r inte unik l�ngre
gradlew clean build (omfattar ju s�klart testfasen)

** K�ra integrationstesterna
gradlew clean build integrationTest
- denna borde ut�kas med l�mplig deploy f�rst