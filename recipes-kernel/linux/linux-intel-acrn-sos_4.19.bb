require linux-intel-acrn.inc

SRC_URI_append = "  file://sos_4.19.scc"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"
