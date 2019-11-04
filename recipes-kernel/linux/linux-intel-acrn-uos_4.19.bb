require linux-intel-acrn.inc
require recipes-kernel/linux/linux-intel_4.19.bb

SRC_URI_append = "  file://uos.cfg"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"
