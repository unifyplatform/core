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

Resultatet av ovanst�ende kommando �r ett modul-id, byt fr�n 190 till det id:t i kommandot nedan
dev:dynamic-import 190

** Eclipse
P� kommandoraden:
  Linux: ./gradlew eclipse
  Windows: gradlew eclipse

I Eclipse:
File -> Import... -> Existing Projects into Workspace

* Bygg och deploya
gradlew clean build

** K�ra de vanliga testerna 
gradlew clean compile test // g�r inte l�ngre - test �r inte unik l�ngre
gradlew clean build (omfattar ju s�klart testfasen)

** K�ra integrationstesterna
// K�r de inbyggda integrationstesterna
gradlew clean build integrationTest (�n s� l�nge kr�ver inte dessa ngn deployad milj�...) 
// K�r ett externt test (inte inbyggt i junit-integrationstester �n)
gradlew clean build devDeploy integrationTestExternal


** Fr�n Jakobs mail - ang VirtualBox och Vagrant:
|+- Jag har lagt upp ett f�rslag nu. Det fungerar s� att servicemix k�rs i en virtuell maskin. 
|+- Installera VirtualBox och Vagrant. Resten �r integrerat med gradle. 
	|+- Anm�rkning - Egil. F�rsta g�ngen finns inte "boxes" el dyl. K�r "vagrant up" fr�n konsolen.
	|+- P� OSX s� fick man en katalog bxes - inte "boxes". Jag k�rde bara "mv bxes boxes" f a f� det som vi v�ntat oss.
|+- Klona core-repot, g� sedan till Jespers exempel och k�r "gradlew server"
|+- D� b�r den installera den virtuella maskinen, bygga exemplet och deploya det till maskinen genom en delad katalog. Ber�tta om det funkar! :-)
|+- Tanken med detta �r att alla kan man utveckla i de verktyg/OS man vill, men n�r kod v�l k�rs s� g�rs det p� exakt samma inst�llningar f�r alla. F�rhoppningsvis minskar antalet problem av karakt�ren "M�rkligt, det funkar p� min dator..." :-)
|+- Hittar ni konstigheter eller vill f�rb�ttra/�ndra i utvecklingsmilj�n s� hittar ni bootstrap-scriptet som installerar allt under /vagrant-data. D�r hittar ni ocks� servicemix_hotdeploy, som �r den delade katalogen som servicemix l�ser in.
|+- Om man vill ominstallera utvecklingsmilj�n s� �r det bara att k�ra gradlew cleanServer, och sedan gradlew server igen.
|
|+- Vill man komma in p� maskinen som k�r servicemix k�r man: "vagrant ssh" 
	|+- V�l inne kan man enkelt komma in i mysql eller servicemix genom tv� alias:
		|+- sql-test (mysql till test-databasen)
		|+- sm (servicemix-client)