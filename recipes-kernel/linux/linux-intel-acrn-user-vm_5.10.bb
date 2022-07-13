require linux-intel-acrn_5.10.inc

SRC_URI:append = "  file://user-vm_5.10.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-user-vm"

SUMMARY = "Linux Kernel with ACRN enabled (User VM)"
