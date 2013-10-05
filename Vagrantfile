# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

# For a complete config reference, see the documentation at vagrantup.com.
Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "base_ubuntu_precise"

  # Install required software upon first run
  config.vm.provision :shell, :path => "./vagrant-data/bootstrap_vagrant.sh"

  # The url from where the 'config.vm.box' box will be fetched if it
  # doesn't already exist on the user's system. This is a blank Ubuntu 12.04 (Precise). 
  config.vm.box_url = "http://www.evitecunify.se/platform-downloads/precise64.box"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network :private_network, ip: "192.168.33.10"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  config.vm.synced_folder "./vagrant-data/servicemix_hotdeploy", "/servicemix_hotdeploy"

  # Use 1GB RAM for virtual machine:
  config.vm.provider :virtualbox do |vb|  
    vb.customize ["modifyvm", :id, "--memory", "1024"]
  end
  
end