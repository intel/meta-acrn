require linux-intel-acrn_5.10.inc

SRC_URI:append = "  file://uos_5.10.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-uos"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"
