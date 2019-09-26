# Getting Started

Really rough getting started brain dump.

### Build Requirements

* openembedded-core, branch master or warrior
* meta-intel, branch master or warrior
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
```

Then this in `conf/multiconfig/uos.conf`:

```
DISTRO = "acrn-demo-uos"
TMPDIR = "${TOPDIR}/master-acrn-uos"
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
-s 7,xhci,1-1,1-2
```

This forwards ports 1-1 and 1-2 into the UOS, which on Skull Canyon is the two front ports (1-1 on the left, 1-2 on the right).  You'll need two input devices for this to work, obviously.


### Install onto NUC

To install the image on to NUC, you could burn the .wic.acrn image to the target NUC internal storage.

Alternatively, you could build a wic based installer image where you can burn the .wic image onto USB flash drive and use USB flash drive as installer. To build the installer image for ACRN, add below lines to `local.conf`:

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

### Things That Break

If a guest kernel sits at `vhm: initializing` and then restarts then I think the problem is that the UOS is trying to boot with the SOS kernel.

If `acrn-dm` segfaults on startup then that is usually that it is trying to do VGT-g but the SOS doesn't have `i915.enable_gvt=1` and friends turned on.
