#!/usr/bin/env bash

add-apt-repository ppa:chris-lea/node.js
sudo add-apt-repository ppa:cwchien/gradle
apt-get update

# Installerar java, behövs för servicemix
apt-get install -y openjdk-7-jre-headless

# Set JAVA_HOME, both for this script. And for the vagrant user on login
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
echo "export JAVA_HOME=${JAVA_HOME}" >> /home/vagrant/.bashrc

###########################
# ServiceMix installation #
###########################
SERVICEMIX_VERSION="4.5.2"
SERVICEMIX_FILE="apache-servicemix-${SERVICEMIX_VERSION}.tar.gz"
SERVICEMIX_HOME="/home/vagrant/apache-servicemix-${SERVICEMIX_VERSION}"
SERVICEMIX_CLIENT="${SERVICEMIX_HOME}/bin/client -a 8101 -h localhost -u smx -p smx"

echo ${SERVICEMIX_FILE}
echo ${SERVICEMIX_CLIENT}

# Download and extract
wget "http://www.evitecunify.se/platform-downloads/${SERVICEMIX_FILE}"
tar -xzvf ${SERVICEMIX_FILE}
rm ${SERVICEMIX_FILE}
# Start
echo "* Start servicemix, and wait 10 seconds for it to start."
${SERVICEMIX_HOME}/bin/start
sleep 10


#########################################
# ServiceMix: install required features #
#########################################
echo "* Installing feature: camel-jdbc"
${SERVICEMIX_CLIENT} "features:install camel-jdbc"
echo "* Installing osgi: commons-dbcp"
COMMONS_DBCP_OUTPUT=$(${SERVICEMIX_CLIENT} "osgi:install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-dbcp/1.4_3")

# last item in array is the bundle number
IFS=' ' read -a OUTPUT_ARRAY <<< "$COMMONS_DBCP_OUTPUT"     # Split output on space
LENGTH=${#OUTPUT_ARRAY[@]}                                  # Get the length.                                          
LAST_POSITION=$((LENGTH - 1))                               # Subtract 1 from the length.                   
BUNDLE_NUM=${OUTPUT_ARRAY[${LAST_POSITION}]}                # Get the last position.
echo "* Found bundle num: ${BUNDLE_NUM} running: 'dev:dynamic-import ${BUNDLE_NUM}'"
${SERVICEMIX_CLIENT} "dev:dynamic-import ${BUNDLE_NUM}"

# Setup a shared folder (vagrant-data/servicemix_hotdeploy) to allow for host system to deploy integrations by simple copy operation
rm -rf ${SERVICEMIX_HOME}/deploy
ln -fs /vagrant/vagrant-data/servicemix_hotdeploy ${SERVICEMIX_HOME}/deploy


########################################
# ServiceMix: enable automatic startup #
########################################
echo "* Installing wrapper feature. OS startup script generator"
${SERVICEMIX_CLIENT} "features:install wrapper"
${SERVICEMIX_CLIENT} "wrapper:install -s AUTO_START -n KARAF -d Karaf -D ServiceMix"

echo "* Stopping service mix, so it can be started by the newly created scripts"
${SERVICEMIX_HOME}/bin/stop
sleep 10

# set vagrant user as owner of the servicemix folder
echo "* Setting servicemix folder permission to vagrant user"
chown -R vagrant apache-servicemix-4.5.2

echo "* Create symbolic link for '/home/vagrant/apache-servicemix-4.5.2/bin/KARAF-service' in '/etc/init.d/'"
ln -fs /home/vagrant/apache-servicemix-4.5.2/bin/KARAF-service /etc/init.d/

echo "* Register script to run on OS startup 'update-rc.d KARAF-service defaults'"
update-rc.d KARAF-service defaults

echo "* Start the service '/etc/init.d/KARAF-service start'"
/etc/init.d/KARAF-service start


#####################
# Apache MySQL, PHP #
#####################
# (We currently only need MySQL, but the others are always good to have.)

MYSQL_PASSWORD="123qwe"
# if apache2 does no exist install (L)AMP stack.
if [ ! -f /etc/apache2/apache2.conf ];
then
  # Install MySQL
  echo "mysql-server-5.5 mysql-server/root_password password ${MYSQL_PASSWORD}" | debconf-set-selections
  echo "mysql-server-5.5 mysql-server/root_password_again password ${MYSQL_PASSWORD}" | debconf-set-selections
  apt-get -y install mysql-client mysql-server-5.5

  # Install Apache2
  apt-get -y install apache2

  # Install PHP5 support
  apt-get -y install php5 \
    libapache2-mod-php5 \
    php5-mysql \
    php5-curl \
    php5-xsl \
    php5-cli

  # Enable mod_rewrite
  a2enmod rewrite

  # Set synced folder as www root
  rm -rf /var/www
  ln -fs /vagrant/vagrant-data/www /var/www

  # Restart services
  service apache2 restart

  # create mysql database
  echo "* Creating DB and table"
  MYSQL='mysql -u root -p${MYSQL_PASSWORD} -e'
  QUERY="CREATE DATABASE IF NOT EXISTS test; use test; CREATE TABLE IF NOT EXISTS INVOICE (id int, time_occurred date, status_code int, sum int)"
  eval $MYSQL "'$QUERY'"
fi

####
# These are currently not needed but might be good to have in a future enviornment setup.
####
#######
# Git #
#######
echo "* Installing git"
if [ -z `which git` ];
then
  apt-get install -y git
fi

##########
# Nodejs #
##########
echo "* Installing node"
if [ -z `which node` ];
then
  apt-get install -y python-software-properties python g++ make
  apt-get install -y nodejs
fi

##########
# Gradle #
##########
echo "* Installing gradle"
if [ -z `which gradle` ];
then
  apt-get install -y gradle
fi

#############################################################
# Setup some aliases to simplify walking around in terminal #
#############################################################
echo "alias sql-test=\"mysql -u root -p${MYSQL_PASSWORD} -h localhost test\"" >> /home/vagrant/.bashrc
echo "alias sm=\"${SERVICEMIX_CLIENT}\"" >> /home/vagrant/.bashrc

