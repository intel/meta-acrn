require acrn-kernel_4.19.inc

SRC_URI_append = "  file://uos_4.19.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-uos"

SUMMARY = "ACRN Kernel (UOS)"
