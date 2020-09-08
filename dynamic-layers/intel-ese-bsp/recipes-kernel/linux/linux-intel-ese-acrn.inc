require recipes-kernel/linux/linux-intel-ese-lts-5.4_git.bb

FILESEXTRAPATHS_prepend := "${LAYERDIR-ese-bsp}/recipes-kernel/linux/linux-config:${LAYERDIR-ese-bsp}/recipes-kernel/linux/files:${LAYERDIR-acrn}/recipes-kernel/linux/files:"

KERNEL_SRC_URI = "git://github.com/intel/linux-intel-lts.git;protocol=https;branch=5.4/yocto;name=machine"


SRCREV_machine = "eeb611e5394c56d45c5cc8f7dc484c9f19e93143"
LINUX_VERSION = "5.4"

KERNEL_PACKAGE_NAME = "kernel"
KERNEL_VERSION_SANITY_SKIP = "1"
KCONF_BSP_AUDIT_LEVEL = "0"
