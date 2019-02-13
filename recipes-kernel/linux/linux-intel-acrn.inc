SUMMARY = "Linux Kernel with ACRN enabled"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRC_URI = "git://github.com/intel/linux-intel-lts.git;protocol=https;branch=${KBRANCH} \
           file://defconfig"

# lts-v4.19.19-base-190208T031538Z
KBRANCH = "4.19/base"
SRCREV = "3647efeb7a27fde8a795ce1af191840430f1be52"
PV = "4.19.19-190208T031538Z"

inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native elfutils-native"
