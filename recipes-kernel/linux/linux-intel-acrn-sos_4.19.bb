require linux-intel-acrn.inc

SRC_URI_append = "  file://sos.cfg"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"
