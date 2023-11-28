SUMMARY = "Linux Kernel 5.19 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/mainline-tracking.git;protocol=https;name=machine;nobranch=1; \
                    "

KMETA_BRANCH = "yocto-5.19"

LINUX_VERSION ?= "5.19.0"
SRCREV_machine ?= "3d4a2611350685c51cd3bb710e29652d18989644"
SRCREV_meta ?= "61d7aaaa97297780205a333d529e55136e20cb11"
