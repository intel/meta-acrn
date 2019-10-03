SUMMARY = "Linux Kernel with ACRN enabled"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRC_URI = "git://github.com/intel/linux-intel-lts.git;protocol=https;branch=${KBRANCH} \
           file://0001-Add-the-plane-restrictionfor-SKL.-Otherwise-there-is.patch \
           file://0002-Add-the-change-for-gvt-g-on-SKL.patch \
           file://defconfig"

# lts-v4.19.73-base-190919T160721Z
KBRANCH = "4.19/base"
SRCREV = "ca05e9cd64b5afbd698fa2707be4d2602890b849"
PV = "4.19.73-base-190919T160721Z"
inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native elfutils-native"
