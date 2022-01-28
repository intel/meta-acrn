# Getting Started

This guide will show how to set up the host machine for Yocto build and then how to build and boot ACRN Hypervisor, Service VM and User VMs on Intel platforms.

## Table of Contents
=================

I.   [Overview](#overview)

II.  [Set Up Build Host](#set-up-build-host)
 * [Compatible Linux Distribution](#compatible-linux-distribution)
 * [Required Packages for Build Host](#required-packages-for-build-host)

III. [Building ACRN Bootable Image](#building-acrn-bootable-image)
 * [Dependencies](#dependencies)
 * [Build Image](#build-image)
   - [Download Layers and Initialize Build Environment](#download-layers-and-initialize-build-environment)
   - [Configure Service VM (SOS)](#configure-service-vm-sos)
   - [Configure Post-launched User VM (UOS)](#configure-post-launched-user-vm-uos)
   - [Build User VM (UOS) image via multiconfig](#build-uos-image-via-multiconfig)
   - [Build ACRN image](#build-acrn-image)

IV.  [Booting ACRN Image](#booting-acrn-image)
 * [Boot ACRN and SOS](#boot-acrn-and-sos)
 * [Launching UOS](#launching-uos)
   - [USB Passthrough](#usb-passthrough)

V.   [Configurations](#configurations)
 * [MACHINE Configuration](#machine-configuration)
 * [Adding Guests](#adding-guests)
 * [ACRN Configuration](#acrn-configuration)
   - [ACRN BOARD Configuration](#acrn-board-configuration)
   - [ACRN SCENARIO Configuration](#acrn-scenario-configuration)
   - [ACRN BUILD MODE Configuration](#acrn-build-mode-configuration)
 * [Kernel Configuration](#kernel-configuration)
   - [SOS](#sos)
   - [UOS](#uos)
 * [GRUB Configuration](#grub-configuration)
 * [Override distro configuration](#override-distro-configuration)

VI.  [Build WIC Installer Image](#build-wic-installer-image)

VII. [Tested Hardware](#tested-hardware)

VIII.[Contributing](#contributing)

## Overview
This layer provides ACRN Hypvervisor integration with Yocto Project.

## Supported Hardware

ACRN is supported on the following Intel platforms:
* Kaby Lake
* Whiskey Lake
* Tiger Lake
* Elkhart Lake

About minimum system requirements and limitations, please find more information at [Supported Hardware](https://projectacrn.github.io/2.7/reference/hardware.html)


## Set Up Build Host

### Compatible Linux Distribution
Make sure your build host meets the following requirements:

- 50 Gbytes of free disk space
- Runs a supported Linux distribution

Currently, the Yocto Project is supported on a number of Linux Distributions. This guide covers only for Ubuntu 18.04 (LTS). To know more about the complete list of    supported Linux distributions, please visit [Supported Linux Distributions](https://www.yoctoproject.org/docs/current/mega-manual/mega-manual.html#detailed-supported-distros)
- Git 1.8.3.1 or greater
- tar 1.28 or greater
- Python 3.5.0 or greater
- gcc 5.0 or greater

### Required Packages for Build Host
Install essential host packages on your build host (Ubuntu):

```
$ sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm
```

## Building ACRN Bootable Image


### Dependencies

meta-acrn layer depends on:
* [poky](https://git.yoctoproject.org/cgit/cgit.cgi/poky), branch master [or honister]
* [meta-oe](https://github.com/openembedded/meta-openembedded/tree/master/meta-oe), branch master [or honister]
* [meta-python](https://github.com/openembedded/meta-openembedded/tree/master/meta-python), branch master [or honinster]
* [meta-filesystems](https://github.com/openembedded/meta-openembedded/tree/master/meta-filesystems), branch master [or honister]
* [meta-networking](https://github.com/openembedded/meta-openembedded/tree/master/meta-networking), branch master [or honister]
* [meta-virtualization](https://git.yoctoproject.org/cgit/cgit.cgi/meta-virtualization), branch master [or honister]
* [meta-intel](https://git.yoctoproject.org/cgit/cgit.cgi/meta-intel), branch master [or honister]

### Build Image

#### Download Layers and Initialize Build Environment

* Create build workspace
```
$ mkdir workspace
```

* Clone poky
```
$ cd workspace
$ git clone https://git.yoctoproject.org/git/poky
```
* Clone all dependent layers
```
$ cd poky
$ git clone https://github.com/openembedded/meta-openembedded.git
$ git clone https://git.yoctoproject.org/git/meta-virtualization
$ git clone https://git.yoctoproject.org/git/meta-intel
$ git clone https://github.com/intel/meta-acrn.git
```
* Initialize build environment
```
$ source oe-init-build-env
```

* Add layers to build environment `conf/bblayers.conf`
```
$ bitbake-layers add-layer ../meta-openembedded/meta-oe
$ bitbake-layers add-layer ../meta-openembedded/meta-python
$ bitbake-layers add-layer ../meta-openembedded/meta-filesystems
$ bitbake-layers add-layer ../meta-openembedded/meta-networking
$ bitbake-layers add-layer ../meta-virtualization
$ bitbake-layers add-layer ../meta-intel
$ bitbake-layers add-layer ../meta-acrn
```

#### Configure Service VM (SOS)

meta-acrn maintains prototype DISTRO configurations for both Servcie VM OS (SOS) and User VM OS (UOS). `acrn-demo-sos` for Service VM OS and `acrn-demo-uos` for User VM OS.
`local.conf` carries the common configuration, which can be overwritten by individual multiconfigs `conf/multiconfig/xxx.conf`
For the Service VM OS, we carry configuration in `conf/local.conf` and for User VM OS we carry in `conf/multiconfig/uos.conf`


Append the following configuration in `conf/local.conf`:

```
MACHINE = "intel-corei7-64"
TMPDIR = "${TOPDIR}/master-acrn-sos"
DISTRO = "acrn-demo-sos"

# Also use the 'uos' configuration
BBMULTICONFIG = "uos"

# The packages-from-images class (container-package.bbclass) needs to know where images are
CONTAINER_PACKAGE_DEPLOY_DIR = "${TOPDIR}/master-acrn-uos/deploy/images/${MACHINE}"
CONTAINER_PACKAGE_MC = "uos"

# Add core-image-base-package to acrn-image-base
IMAGE_INSTALL:append:pn-acrn-image-base = " core-image-base-package"
# Add core-image-weston-package to acrn-image-sato
IMAGE_INSTALL:append:pn-acrn-image-sato = " core-image-weston-package"

# set preferred kernel for sos
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
```

#### Configure Post-launched User VM (UOS)

Add the following in `conf/multiconfig/uos.conf`:

```
DISTRO = "acrn-demo-uos"
TMPDIR = "${TOPDIR}/master-acrn-uos"
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-uos"
```

> Note how the parent `local.conf` refers to what `DEPLOY_DIR_IMAGE` will be in `uos.conf`.  Remember to keep these in sync.

#### Build ACRN image

> Based on your target image, It can be:
>  - acrn-image-base
>  - acrn-image-sato
>  - acrn-image-weston
>  - acrn-image-minimal

```
$ bitbake acrn-image-base
```

By default, building `acrn-image-base` will build a `.wic` image and can be located at `build/master-acrn-sos/deploy/images/intel-corei7-64/`.

Based on configuration, it will also build post-launched VM and install on acrn-image-*.wic along with launch script. Separately user VMs can be located at `build/master-acrn-uos/deploy/images/intel-corei7-64/`. User VM images can be core-image-base, core-image-sato and core-image-weston.

## Booting ACRN Image

### Boot ACRN and SOS

On successful build, you will find the ACRN bootable image `acrn-image-base-intel-corei7-64.wic` in the `build/master-acrn-sos/deploy/images/intel-corei7-64/` directory.

Under Linux, insert a USB flash drive.  Assuming the USB flash drive
takes device `/dev/sdf`, use `dd` to copy the image to it.  Before the image
can be burned onto a USB drive, it should be un-mounted. Some Linux distros
may automatically mount a USB drive when it is plugged in. Using USB device
/dev/sdf as an example, find all mounted partitions:
```
    $ mount | grep sdf
```
and un-mount those that are mounted, for example:
```
    $ umount /dev/sdf1
    $ umount /dev/sdf2
```
Now burn the image onto the USB drive:
```
    $ sudo dd if=acrn-image-base-intel-corei7-64.wic of=/dev/sdf status=progress
    $ sync
    $ eject /dev/sdf
```
This should give you a bootable USB flash device.  Insert the device into a bootable USB socket on the target, and power on. It should give two boot options, 'boot' and 'ACRN (Yocto)'. Select 'ACRN (Yocto)' to spawn hypervisor, while 'boot' to boot as normal Linux.

### Launching UOS

```
$ /var/lib/machines/launch-base.sh
```

#### USB Passthrough

Once a graphical UOS has been started you'll want to interact with it.  One simple solution is to set the relevant variables so that logging into the console can access the display.

For X:

```
$ export DISPLAY=:1
```

And for Weston:

```
$ export XDG_RUNTIME_DIR=/run/user/$(id -u)
```

That will let you start applications but you still can't interact with them: good enough for running glmark2 but not for anything interactive.  For this, USB host virtualisation lets you pass specific USB ports through to the guest.  Start by using `lsusb` to identify the bus and port you want to forward, and then edit `launch-weston.sh` or similar to pass another option to `acrn-dm`:

```
-s 7,xhci,1-1,1-2
```

This forwards ports 1-1 and 1-2 into the UOS, which on Skull Canyon is the two front ports (1-1 on the left, 1-2 on the right).  You'll need two input devices for this to work, obviously.



## Configurations

### MACHINE Configuration

[meta-intel](https://git.yoctoproject.org/cgit/cgit.cgi/meta-intel) provides the following machine configurations:

* intel-corei7-64

* intel-skylake-64

> intel-skylake-64 is 64-bit machine with -march=skylake and avx2 instruction-set set up. For more information, please check [intel-skylake-64.conf](http://git.yoctoproject.org/cgit/cgit.cgi/meta-intel/tree/conf/machine/intel-skylake-64.conf)

> intel-skylake-64 machine must be used for Skylake and successor platforms


To configure MACHINE, set the following in your `conf/local.conf`
```
MACHINE = "intel-corei7-64"
```

### Adding Guests

The multiconfig/package magic works with a `<image>-package.bb` recipe that inherits `container-package`. This puts the wic image and launcher script into a package which can be added as usual.

To add core-image-base.wic image into Service OS (Add core-image-base-package to your target ACRN image)
```
IMAGE_INSTALL:append:pn-acrn-image-base = " core-image-base-package"
```

To add core-image-weston-package to acrn-image-weston
```
IMAGE_INSTALL:append:pn-acrn-image-weston = " core-image-weston-package"
```

To add core-image-weston-package to acrn-image-base
```
IMAGE_INSTALL:append:pn-acrn-image-base = " core-image-weston-package"
```

### ACRN Configuration

#### ACRN BOARD Configuration

To build for your target board, set `ACRN_BOARD` in your `conf/local.conf`. By default it is set to `nuc11tnbi5`
```
ACRN_BOARD = "whl-ipc-i5"
```

Supported Boards:
- nuc11tnbi5
- cfl-k700-i7
- whl-ipc-i5

For More information, Please check [Supported Hardware](https://projectacrn.github.io/2.7/reference/hardware.html)

#### ACRN SCENARIO Configuration

To build for your acrn scenario, set `ACRN_SCENARIO` in your `conf/local.conf`. By default it is set to `shared` scenario.
```
ACRN_SCENARIO  = "hybrid"
```
Supported scenarios:
- sdc
- shared
- partitioned
- hybrid
- hybrid_rt

For more information, please check [Generate a Scenario Configuration File and Launch Scripts](https://projectacrn.github.io/2.7/getting-started/getting-started.html#generate-a-scenario-configuration-file-and-launch-scripts)

To customize ACRN Configruation, please check [Introduction to ACRN Configuration](https://projectacrn.github.io/2.7/tutorials/acrn_configuration_tool.html)

#### ACRN BUILD MODE Configuration

To build ACRN in `RELEASE` mode, set `y` to `ACRN_RELEASE` in your `conf/local.conf`. By default it is set to `n`
```
ACRN_RELEASE = "y"
```

To build ACRN in `DEBUG` mode, set `n` to `ACRN_RELEASE` in your `conf/local.conf`.
```
ACRN_RELEASE = "n"
```

### Kernel Configuration

There are multiple kernel variant available for both SOS and UOS.

#### SOS

To switch to linux-intel-acrn-sos LTS 5.10 kernel (default), in 'local.conf' replace with the following lines:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
PREFERRED_VERSION_linux-intel-acrn-sos = "5.10%"
```

#### UOS


To switch to linux-intel-acrn-uos LTS 5.10 kernel (default), in 'conf/multiconfig/uos.conf' replace with following lines:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-uos"
PREFERRED_VERSION_linux-intel-acrn-uos = "5.10%"
```

To switch to linux-intel-rt-acrn-uos Preempt-RT 5.10 kernel, in 'conf/multiconfig/uos.conf' replace with following line:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-rt-acrn-uos"
PREFERRED_VERSION_linux-intel-rt-acrn-uos = "5.10%"
```

To switch to linux-intel-rt-acrn-uos Preempt-RT 5.4 kernel, in 'conf/multiconfig/uos.conf' replace with following line:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-rt-acrn-uos"
PREFERRED_VERSION_linux-intel-rt-acrn-uos = "5.4%"
```

### GRUB Configuration

Following variables are used to prepare kernel command lines for Pre-launched User VMs and Service VM. Based on these variables an ACRN specific `grub.cfg` gets generated.
These variables can be overwritten in your local config.

* VMFLAGS - list of pre-launched VMs including Service VM
    For example: VMFLAGS = "vm0 vm1 ... vmx"

* VM_APPEND - VM kernel commandline
* KERNEL_IMAGE - kernel image like bzImage for each vm
* KERNEL_MOD - kern_mod tag in acrn scenario xml for each
* ACPI_TAG - ACPI tag for specific VM
* ACPI_BIN - binary of ACPI tables for a specific vm
* PART_LABEL - Partition Label, if set, it allow grub bootloader to pick modules/kernel binaries by partition label


For example, using hybrid scenario for nuc7i7dnb:

     VMFLAGS = "vm0 vm1"
     # vm0 (prelaunch vm zephyr)
     VM_APPEND_vm0 = "xxx"
     KERNEL_IMAGE_vm0 = "/custom path/zephyr.bin"
     KERNEL_MOD_vm0 = "Zephyr_RawImage"
     ACPI_TAG_vm0 = "ACPI_VM0"
     ACPI_BIN_vm0 = "ACPI_VM0.bin"
     PART_LABEL_vm0 = "zephyr module partition label"

     # vm1 (sos)
     VM_APPEND_vm1 = "xxx"
     KERNEL_IMAGE_vm1 = "/custom path/bzImage" //Pass complete path
     KERNEL_MOD_vm1 = "Linux_bzImage"
     PART_LABEL_vm1 = "boot"

* ACRN_EFI_GRUB2_MOD_CFG wic variable (semicolon (;) separated list)
    to make additional entries in grub.cfg i.e insmod ext3

Additionaly grub bootloader uses 'search' command at grub menu entry to find the device by file (--file).

* ACRN_HV_EFI_CFG wic variable to add ACRN Hyperviosr parameters

For more information, please check [Update Ubuntu GRUB](https://projectacrn.github.io/2.7/tutorials/using_hybrid_mode_on_nuc.html#update-ubuntu-grub),  [acrn-bootconf.bbclass](https://github.com/intel/meta-acrn/blob/master/classes/acrn-bootconf.bbclass) and [ACRN Hypervisor Parameters](https://projectacrn.github.io/latest/user-guides/hv-parameters.html#acrn-hypervisor-parameters)


### Override distro configuration

Due to parsing sequence conflict with `meta-intel`, weak assignments are not used in `acrn-demo-sos` and `acrn-demo-uos` distros. So to override distro configuration in `conf/local.conf`, override syntax can be used i.e
```
WKS_FILE_acrn-demo-sos = "your-custom.wks.in"
```


## Build WIC Installer Image

To install the image on to NUC, you could burn the .wic image to the target hardware's internal storage.

Alternatively, you could build a wic based installer image where you can burn the .wic image onto USB flash drive and use USB flash drive as installer. To build the installer image for ACRN, add the following lines to `conf/local.conf`:

```
BBMULTICONFIG:append  = " installer "
```

Add followings in `conf/multiconfig/installer.conf`:

```
# use the installer wks file
WKS_FILE:pn-acrn-image-base = "image-installer.wks.in"

# build initramsfs to start the installation
INITRD_IMAGE_LIVE="core-image-minimal-initramfs"

# make sure initramfs and ext4 image are ready before building wic image
do_image_wic[depends] += "${INITRD_IMAGE_LIVE}:do_image_complete"
IMAGE_TYPEDEP_wic = "ext4"

# content to be installed
IMAGE_BOOT_FILES:append = "\
    ${KERNEL_IMAGETYPE} \
    acrn.bin;esp/acrn.bin \
    microcode.cpio;esp/microcode.cpio \
    grub-efi-bootx64.efi;EFI/BOOT/bootx64.efi \
    ${IMAGE_ROOTFS}/boot/EFI/BOOT/grub.cfg;esp/EFI/BOOT/grub.cfg \
    ${IMGDEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.ext4;rootfs.img \
"
```

Now build the installer image:

```
$ bitbake mc:installer:acrn-image-base
```

> Currently ACRN WIC Installer is supported only OE-Core(poky) `master`, `hardknott` and `gatesgarth`. Support for `dunfell` is still in progress.

## Tested Hardware
The following undergo regular basic testing with their respective MACHINE types.

intel-corei7-64:
    NUC11TNHi5

intel-skylake-64:
    NUC11TNHi5


## Contributing
You are encouraged to follow Github Pull request workflow to share changes and following commit message guidelines are recommended [OE patch guidelines](https://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines)

Layer Maintainer: Naveen Saini \<naveen.kumar.saini@intel.com\>
