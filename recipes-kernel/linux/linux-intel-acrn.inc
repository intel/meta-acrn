SUMMARY = "Linux Kernel with ACRN enabled"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KERNEL_SRC_URI ?= "git://github.com/projectacrn/acrn-kernel.git;protocol=https;branch=master;name=machine"
SRC_URI = "${KERNEL_SRC_URI}"

SRC_URI_append = " file://defconfig"
SRCREV_machine ?= "acrn-2020w07.3-140000p"


inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native elfutils-native"
