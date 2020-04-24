require linux-intel-acrn_4.19.inc

SRC_URI_append = "  file://uos_4.19.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-uos"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"
