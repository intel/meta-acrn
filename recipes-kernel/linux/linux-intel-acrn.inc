SUMMARY = "Linux Kernel with ACRN enabled"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRC_URI = "git://github.com/intel/linux-intel-lts.git;protocol=https;branch=${KBRANCH} \
           file://0001-Add-the-plane-restrictionfor-SKL.-Otherwise-there-is.patch \
           file://0002-Add-the-change-for-gvt-g-on-SKL.patch \
           file://defconfig"

# lts-v4.19.36-base-190429T152313Z
KBRANCH = "4.19/base"
SRCREV = "99a52e81ddce86d4b03b46a6d6cdc62bc3c8e07e"
PV = "4.19.36-190429T152313Z"
inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native elfutils-native"

