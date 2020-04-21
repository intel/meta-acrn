require acrn-kernel.inc

SRC_URI_append = "  file://sos_4.19.scc"

LINUX_VERSION_EXTENSION ?= "-acrn-kernel-sos"

SUMMARY = "ACRN Kernel (SOS)"
