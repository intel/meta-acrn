KERNEL_SRC_URI ?= "git://github.com/projectacrn/acrn-kernel.git;protocol=https;branch=4.19/preempt-rt;name=machine"
SRC_URI = "${KERNEL_SRC_URI}"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-intel-rt-acrn-uos:"

SRC_URI_append = " file://defconfig"
SRCREV_machine ?= "acrn-2019w45.5-143000p"

LINUX_VERSION ?= "4.19.72"
LINUX_KERNEL_TYPE = "standard"
KERNEL_PACKAGE_NAME = "kernel"
LICENSE = "GPLv2"

inherit kernel

S = "${WORKDIR}/git"
DEPENDS += "lz4-native elfutils-native"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

