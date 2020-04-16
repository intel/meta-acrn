require recipes-kernel/linux/linux-intel_5.4.bb

SRC_URI_append = "  file://sos_5.4.cfg"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"

KERNEL_EXTRA_FEATURES += " cfg/hv-guest.scc  cfg/paravirt_kvm.scc sos_5.4.cfg "
