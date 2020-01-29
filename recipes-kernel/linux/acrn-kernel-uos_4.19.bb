require acrn-kernel.inc

SRC_URI_append = "  file://uos.cfg"

LINUX_VERSION_EXTENSION ?= "-acrn-kernel-uos"

SUMMARY = "ACRN Kernel (UOS)"
