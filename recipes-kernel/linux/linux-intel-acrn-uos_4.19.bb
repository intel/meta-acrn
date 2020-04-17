require linux-intel-acrn.inc

SRC_URI_append = "  file://uos.cfg"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-uos"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"
