require acrn-kernel_5.4.inc

SRC_URI:append = "  file://user-vm_5.4.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-user-vm"

SUMMARY = "ACRN Kernel (User VM)"
