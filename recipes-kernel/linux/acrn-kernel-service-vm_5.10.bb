require acrn-kernel_5.10.inc

SRC_URI:append = "  file://service-vm_5.10.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-service-vm"

SUMMARY = "ACRN Kernel (Service VM)"

KERNEL_FEATURES:append = " service-vm_5.10.scc "
