SUMMARY = "Linux Kernel with ACRN enabled"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRC_URI = "git://github.com/intel/linux-intel-lts.git;protocol=https;branch=${KBRANCH} \
           file://defconfig"

# lts-v4.19.23-base-190301T095825Z
KBRANCH = "4.19/base"
SRCREV = "99e4e919067a3a4825ebfb1dc612d329efe692b4"
PV = "4.19.23-190301T095825Z"

inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native elfutils-native"

