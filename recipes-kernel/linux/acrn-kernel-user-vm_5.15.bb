require acrn-kernel_5.15.inc

SRC_URI:append = "  file://user-vm_5.15.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-user-vm"

SUMMARY = "ACRN Kernel (User VM)"

KERNEL_FEATURES:append = " user-vm_5.15.scc "
