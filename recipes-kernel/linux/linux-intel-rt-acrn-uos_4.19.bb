SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (UOS)"

require recipes-kernel/linux/linux-intel.inc
require linux-intel-acrn.inc

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-rt-acrn-uos":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-rt-acrn-uos to enable it")
}

SRC_URI_append = "  file://uos.cfg"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
SRC_URI_append = " file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch"

KBRANCH = "4.19/preempt-rt"
KMETA_BRANCH = "yocto-4.19"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION ?= "4.19.59"
SRCREV_machine ?= "d6dc301c71aea6098e5e19ef3f265d0ba9981167"
SRCREV_meta ?= "960be4218436fbbb3500e019f7abf02fa94e6aac"

LINUX_VERSION_EXTENSION = "-linux-intel-preempt-rt-acrn-uos"
