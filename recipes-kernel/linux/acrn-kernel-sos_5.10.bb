require acrn-kernel_5.10.inc

SRC_URI_append = "  file://sos_5.10.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-sos"

SUMMARY = "ACRN Kernel (SOS)"

KERNEL_FEATURES_append = " sos_5.10.scc "
