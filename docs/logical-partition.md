# Setup for Logical partition

Example on build and setup for logical partition, these are based on default configuration from acrn-hypervisor for nuc7i7dnb logical_partition scenario. In this case, vm0 have its rootfs on SATA while vm1 on USB storage. The target device/Nuc should set to boot from SATA first.

### Setup
add multiconfig and common setting for both vm in local.conf
```
MACHINE = "intel-corei7-64"
DISTRO = "acrn-demo-uos"
BBMULTICONFIG = "vm0 vm1"

# update fstab in this case can cause problem to mount rootfs
WIC_CREATE_EXTRA_ARGS = " --no-fstab-update "
```

setting for VM0 in 'conf/multiconfig/vm0.conf'
```
TMPDIR = "${TOPDIR}/master-acrn-vm0"
ACRN_SCENARIO = "logical_partition"

EFI_PROVIDER = "grub-efi"
GRUB_BUILDIN:append = " multiboot2 "

# required setting for ESP
WICVARS:append = " ACRN_EFI_BOOT_CONF "
ACRN_EFI_BOOT_CONF = "${KERNEL_IMAGETYPE}:Linux_bzImage:root=/dev/sda2 ${APPEND};ACPI_VM0.bin:ACPI_VM0;ACPI_VM1.bin:ACPI_VM1;"
IMAGE_EFI_BOOT_FILES = "ACPI_VM0.bin ACPI_VM1.bin"
WKS_FILE = "acrn-bootdisk-microcode.wks.in"
```

setting for VM1 in 'conf/multiconfig/vm1.conf'
```
# output vm1 image to another directory
TMPDIR = "${TOPDIR}/master-acrn-vm1"
```

In this case both vm are User VM, build acrn-hypervisor separately so it can be inject into VM0 ESP.
```
$ bitbake multiconfig:vm0:acrn-hypervisor
$ bitbake multiconfig:vm0:core-image-base
$ bitbake multiconfig:vm1:core-image-base
```

flash vm0 wic image to SATA device and flash vm1 wic image to USB storage.
