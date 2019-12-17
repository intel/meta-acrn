SUMMARY = "Linux Kernel with ACRN enabled"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KERNEL_SRC_URI ?= "git://github.com/projectacrn/acrn-kernel.git;protocol=https;branch=release_1.4;name=machine"
SRC_URI = "${KERNEL_SRC_URI}"

SRC_URI_append = " file://defconfig"
SRCREV_machine ?= "1dc26c7b52f5366a0859c7d4b0d52d0232c46104"


inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native elfutils-native"
