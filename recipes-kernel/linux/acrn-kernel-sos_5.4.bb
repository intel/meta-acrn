require acrn-kernel_5.4.inc

SRC_URI_append = "  file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-sos"

SUMMARY = "ACRN Kernel (SOS)"

KERNEL_FEATURES_append = " sos_5.4.scc "
