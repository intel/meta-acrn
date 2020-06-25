# Getting Started

Really rough getting started brain dump.

### Build Requirements

* openembedded-core, branch master or dunfell
* meta-intel, branch master or dunfell
* meta-acrn, branch master

### Setup

There is now a prototype example distro called `acrn-demo` that uses multiconfig to build the SOS and UOS. Something like this in `local.conf`:

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
IMAGE_INSTALL_append_pn-acrn-image-base = " core-image-base-package"
# Add core-image-weston-package to acrn-image-sato
IMAGE_INSTALL_append_pn-acrn-image-sato = " core-image-weston-package"

# set preferred kernel for sos
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
```

Then this in `conf/multiconfig/uos.conf`:

```
DISTRO = "acrn-demo-uos"
TMPDIR = "${TOPDIR}/master-acrn-uos"
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-uos"
```

Note how the parent `local.conf` refers to what `DEPLOY_DIR_IMAGE` will be in `uos.conf`.  Remember to keep these in sync.

Test that you can build multiconfigs:

```
$ bitbake multiconfig:uos:core-image-base
```

This should build you a `core-image-base.ext4` in the UOS work directory. Now build your acrn image:

```
$ bitbake acrn-image-base
```

Note that thanks to a bug in bitbake if you go straight to `acrn-image-base` from an empty sstate then it will build a lot of recipes twice.  For speed, build the UOS image first and then the SOS, as the SOS image can re-use 99% of the sstate.

Building `acrn-image-base` will build a `wic.acrn` image that on first boot will be normal Linux but will setup EFI entries so that subsequent boots are inside ACRN. Alternatively use the EFI shell, assuming you've got the image on a USB stick something like this works:

```
> fs1:
> \EFI\BOOT\acrn.efi
```

GVT requires kernel options, these are enabled by default in the `acrn-demo-sos` distro. If these options cause problems then `LINUX_GVT_APPEND` can be overridden.

### Kernel selection
There are multiple kernel variant available for both SOS and UOS.

#### SOS

To switch to linux-intel-acrn-sos LTS 5.4 kernel (default), in 'local.conf' replace with below lines:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
PREFERRED_VERSION_linux-intel-acrn-sos = "5.4%"
```

To switch to linux-intel-acrn-sos LTS 4.19 kernel, in 'local.conf' replace with below lines:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
PREFERRED_VERSION_linux-intel-acrn-sos = "4.19%"
```

#### UOS


To switch to linux-intel-acrn-uos LTS 5.4 kernel (default), in 'conf/multiconfig/uos.conf' replace with below lines:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-uos"
PREFERRED_VERSION_linux-intel-acrn-uos = "5.4%"
```

To switch to linux-intel-acrn-uos LTS 4.19 kernel, in 'conf/multiconfig/uos.conf' replace with below lines:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-uos"
PREFERRED_VERSION_linux-intel-acrn-uos = "4.19%"
```

To switch to linux-intel-rt-acrn-uos Preempt-RT 5.4 kernel (default), in 'conf/multiconfig/uos.conf' replace with below line:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-rt-acrn-uos"
PREFERRED_VERSION_linux-intel-rt-acrn-uos = "5.4%"
```

To switch to linux-intel-rt-acrn-uos Preempt-RT 4.19 kernel, in 'conf/multiconfig/uos.conf' replace with below line:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-rt-acrn-uos"
PREFERRED_VERSION_linux-intel-rt-acrn-uos = "4.19%"
```

### Acrn libvirt configuration
libvirt (acrn-libvirt) is currenly supported by linux-intel-kernel-sos 5.4% and ACRN 2.0. It should be build only for SOS.
To build libvirt recipe, add meta-virtualization and its dependency layers in bblayers.conf.
To build and ship in SOS image, add below line in local.conf:

```
# Add to acrn-image-base
IMAGE_INSTALL_append_pn-acrn-image-base = " libvirt libvirt-libvirtd libvirt-virsh"
# Add to acrn-image-sato
IMAGE_INSTALL_append_pn-acrn-image-sato = " libvirt libvirt-libvirtd libvirt-virsh"
# Add to acrn-image-weston
IMAGE_INSTALL_append_pn-acrn-image-weston = " libvirt libvirt-libvirtd libvirt-virsh"
```

### Adding Guests

The multiconfig/package magic works with a `<image>-package.bb` recipe that inherits `container-package`. This puts the image, kernel, and launcher script into a package which can be added as usual.

### USB Passthrough

Once a graphical UOS has been started you'll want to interact with it.  One simple solution is to set the relevant variables so that logging into the console can access the display.  For X:

```
$ export DISPLAY=:1
```

And for Weston:

```
$ export XDG_RUNTIME_DIR=/run/user/$(id -u)
```

That will let you start applications but you still can't interact with them: good enough for running glmark2 but not for anything interactive.  For this, USB host virtualisation lets you pass specific USB ports through to the guest.  Start by using `lsusb` to identify the bus and port you want to forward, and then edit `launch-weston.sh` or similar to pass another option to `acrn-dm`:

```
-s 8,xhci,1-2,1-3
```

This forwards ports 1-2 and 1-3 into the UOS, which on NUC7I7DNH1E is the two front ports (1-2 on the left, 1-3 on the right). 


### Install onto NUC

To install the image on to NUC, you could burn the .wic.acrn image to the target NUC internal storage.

Alternatively, you could build a wic based installer image where you can burn the .wic image onto USB flash drive and use USB flash drive as installer. To build the installer image for ACRN, add below lines to `local.conf`:

```
BBMULTICONFIG_append  = " installer "
```

Then this in `conf/multiconfig/installer.conf`:

```
# use the installer wks file
WKS_FILE = "image-installer.wks.in"

# do not need to convert wic to wic.acrn
IMAGE_FSTYPES_remove="wic.acrn"

# build initramsfs to start the installation
INITRD_IMAGE_LIVE="core-image-minimal-initramfs"

# make sure initramfs and ext4 image are ready before building wic image
do_image_wic[depends] += "${INITRD_IMAGE_LIVE}:do_image_complete"
IMAGE_TYPEDEP_wic = "ext4"

# content to be install
IMAGE_BOOT_FILES_append = "\
    ${KERNEL_IMAGETYPE} \
    microcode.cpio \
    acrn.efi;EFI/BOOT/acrn.efi \
    systemd-bootx64.efi;EFI/BOOT/bootx64.efi \
    ${IMAGE_ROOTFS}/boot/loader/loader.conf;loader/loader.conf \
    ${IMAGE_ROOTFS}/boot/loader/entries/boot.conf;loader/entries/boot.conf \
    ${IMGDEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.ext4;rootfs.img \
"
```

Now build the installer image:

```
$ bitbake mc:installer:acrn-image-base
```

### Things That Break

If a guest kernel sits at `vhm: initializing` and then restarts then I think the problem is that the UOS is trying to boot with the SOS kernel.

If `acrn-dm` segfaults on startup then that is usually that it is trying to do VGT-g but the SOS doesn't have `i915.enable_gvt=1` and friends turned on.
