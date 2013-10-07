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

** Köra de vanliga testerna 
gradlew clean compile test // går inte längre - test är inte unik längre
gradlew clean build (omfattar ju såklart testfasen)

** Köra integrationstesterna
// Kör de inbyggda integrationstesterna
gradlew clean build integrationTest (än så länge kröver inte dessa ngn deployad miljö...) 
// Kör ett externt test (inte inbyggt i junit-integrationstester än)
gradlew clean build devDeploy integrationTestExternal


** Från Jakobs mail - ang VirtualBox och Vagrant:
|+- Jag har lagt upp ett förslag nu. Det fungerar så att servicemix körs i en virtuell maskin. 
|+- Installera VirtualBox och Vagrant. Resten är integrerat med gradle. 
	|+- Anmärkning - Egil. Första gången finns inte "boxes" el dyl. Kör "vagrant up" från konsolen.
	|+- På OSX så fick man en katalog bxes - inte "boxes". Jag körde bara "mv bxes boxes" f a få det som vi väntat oss.
|+- Klona core-repot, gå sedan till Jespers exempel och kör "gradlew server"
|+- Då bör den installera den virtuella maskinen, bygga exemplet och deploya det till maskinen genom en delad katalog. Berätta om det funkar! :-)
|+- Tanken med detta är att alla kan man utveckla i de verktyg/OS man vill, men när kod väl körs så görs det på exakt samma inställningar för alla. Förhoppningsvis minskar antalet problem av karaktären "Märkligt, det funkar på min dator..." :-)
|+- Hittar ni konstigheter eller vill förbättra/ändra i utvecklingsmiljön så hittar ni bootstrap-scriptet som installerar allt under /vagrant-data. Där hittar ni också servicemix_hotdeploy, som är den delade katalogen som servicemix läser in.
|+- Om man vill ominstallera utvecklingsmiljön så är det bara att köra gradlew cleanServer, och sedan gradlew server igen.
|
|+- Vill man komma in på maskinen som kör servicemix kör man: "vagrant ssh" 
	|+- Väl inne kan man enkelt komma in i mysql eller servicemix genom två alias:
		|+- sql-test (mysql till test-databasen)
		|+- sm (servicemix-client)