require linux-intel-acrn.inc
require recipes-kernel/linux/linux-intel_4.19.bb

SRC_URI_append = "  file://uos.cfg"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-uos"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"
