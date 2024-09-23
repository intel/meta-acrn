require linux-intel-acrn_6.1.inc

SRC_URI:append = "  file://user-vm_6.1.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-user-vm"

SUMMARY = "Linux Kernel with ACRN enabled (User VM)"
