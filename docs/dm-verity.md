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
DISTRO = "acrn-demo-service-vm"

CONTAINER_PACKAGE_DEPLOY_DIR = "${TOPDIR}/master-acrn-uos/deploy/images/${MACHINE}"
CONTAINER_PACKAGE_MC = "uos"

PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-service-vm"
PREFERRED_VERSION_linux-intel-acrn-service-vm = "5.10%"

IMAGE_CLASSES += "dm-verity-img"

#ACRN image, which you want to build i.e acrn-image-base, acrn-image-sato, acrn-image-weston etc
DM_VERITY_IMAGE = "acrn-image-*"
DM_VERITY_IMAGE_TYPE = "ext4"

INITRAMFS_IMAGE = "dm-verity-image-initramfs"
INITRAMFS_FSTYPES = "cpio.gz"
INITRAMFS_IMAGE_BUNDLE = "1"

# list of pre-launched vm including Service vm
VMFLAGS = " vm0 "

# update the ACRN_EFI_BOOT_CONF for the kernel image with initramfs bundled
# default value for vm0 based on industry scenario for nuc7i7dnb
VM_APPEND_vm0 = "${APPEND}"
KERNEL_IMAGE_vm0 = "${KERNEL_IMAGETYPE}-${INITRAMFS_LINK_NAME}.bin"
KERNEL_MOD_vm0 = "Linux_bzImage"
```

conf/local.conf should enable multiconfig build for Service VM (sos)

```
BBMULTICONFIG = "sos"

# Or, to buid User VM too
BBMULTICONFIG = "sos uos"
```

Trigger bitbake to build acrn image:
```
$ bitbake mc:sos:acrn-image-base

```

On Boot of *.wic image, rootfs filesystem will be Read-only.
