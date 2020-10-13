To enable Dm-verity for service OS:

Add meta-security and depedency layers in bblayer.conf

  ~/meta-intel \
  ~/meta-acrn \
  ~/meta-openembedded/meta-oe \
  ~/meta-openembedded/meta-filesystems \
  ~/meta-openembedded/meta-python \
  ~/meta-openembedded/meta-networking \
  ~/meta-openembedded/meta-perl \
  ~/meta-virtualization \
  ~/meta-security \


Create conf/multiconfig/sos.conf.


```
MACHINE = "intel-corei7-64"
TMPDIR = "${TOPDIR}/master-acrn-sos"
DISTRO = "acrn-demo-sos"

CONTAINER_PACKAGE_DEPLOY_DIR = "${TOPDIR}/master-acrn-uos/deploy/images/${MACHINE}"
CONTAINER_PACKAGE_MC = "uos"

PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
PREFERRED_VERSION_linux-intel-acrn-sos = "5.4%"

IMAGE_CLASSES += "dm-verity-img"

#ACRN image, which you want to build i.e acrn-image-base, acrn-image-sato, acrn-image-weston etc
DM_VERITY_IMAGE = "acrn-image-*"
DM_VERITY_IMAGE_TYPE = "ext4"

INITRAMFS_IMAGE = "dm-verity-image-initramfs"
INITRAMFS_FSTYPES = "cpio.gz"
INITRAMFS_IMAGE_BUNDLE = "1"
WKS_FILE = "systemd-bootdisk-dmverity.wks.in"
```

conf/local.conf should enable multiconfig build for sos

```
BBMULTICONFIG = "sos"

# Or, to buid uos too
BBMULTICONFIG = "sos uos"
```

Trigger bitbake to build acrn image:
```
$ bitbake mc:sos:acrn-image-base

```

On Boot of *wic.acrn image, rootfs filesystem will be Read-only.
