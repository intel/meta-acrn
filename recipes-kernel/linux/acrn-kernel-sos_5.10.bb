require acrn-kernel_5.10.inc

SRC_URI:append = "  file://sos_5.10.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-sos"

SUMMARY = "ACRN Kernel (SOS)"

KERNEL_FEATURES:append = " sos_5.10.scc "
