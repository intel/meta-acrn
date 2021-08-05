require linux-intel-acrn_5.4.inc

SRC_URI:append = "  file://uos_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-uos"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"
