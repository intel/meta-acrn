require acrn-kernel_5.10.inc

SRC_URI_append = "  file://uos_5.10.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-uos"

SUMMARY = "ACRN Kernel (UOS)"

KERNEL_FEATURES_append = " uos_5.10.scc "
