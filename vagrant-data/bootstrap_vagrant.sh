#!/usr/bin/env bash



apt-get update                                  # Get updated repo content
apt-get install -y python-software-properties   # Install add-apt-repository (for Ubuntu <= 12.04)
#apt-get install -y software-properties-common  # Install add-apt-repository (for ubuntu >= 12.10)
add-apt-repository ppa:chris-lea/node.js        # Repo for Nodejs
add-apt-repository ppa:cwchien/Gradle           # Repo for gradle
apt-get update                                  # Refresh apt-get again after adding new repos

apt-get install -y openjdk-7-jre-headless                     # Install java 7
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64            # Set JAVA_HOME, for this script.
echo "export JAVA_HOME=${JAVA_HOME}" >> /home/vagrant/.bashrc # Set JAVA_HOME, for the vagrant user on login

###########################
# ServiceMix installation #
###########################
SERVICEMIX_VERSION="4.5.2"
#SERVICEMIX_VERSION="5.0.0-SNAPSHOT"
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
echo "* Start servicemix."
${SERVICEMIX_HOME}/bin/start


#########################################
# ServiceMix: install required features #
#########################################
echo "* Installing feature: camel-jdbc. Retrying until servicemix is up"
until ${SERVICEMIX_CLIENT} "features:install camel-jdbc" # Repeating this command until it is successful
do
  echo "Trying again in 2 sec."
  sleep 2
done
echo "EXECUTING: ${SERVICEMIX_CLIENT} features:install camel-jdbc"
${SERVICEMIX_CLIENT} "install -s mvn:mysql/mysql-connector-java/5.1.18"
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
chown -R vagrant ${SERVICEMIX_HOME}

echo "* Create symbolic link for '/home/vagrant/apache-servicemix-XXXXXX/bin/KARAF-service' in '/etc/init.d/'"
ln -fs ${SERVICEMIX_HOME}/bin/KARAF-service /etc/init.d/

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
  apt-get install -y python g++ make
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

