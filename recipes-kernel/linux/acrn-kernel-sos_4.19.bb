require acrn-kernel.inc

SRC_URI_append = "  file://sos.cfg"

LINUX_VERSION_EXTENSION ?= "-acrn-kernel-sos"

SUMMARY = "ACRN Kernel (SOS)"
