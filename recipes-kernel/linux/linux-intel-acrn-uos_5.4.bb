require recipes-kernel/linux/linux-intel_5.4.bb

SRC_URI_append = "  file://uos_5.4.scc"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-uos"

SUMMARY = "Linux Kernel with ACRN enabled (UOS)"

KERNEL_FEATURES_append = " cfg/hv-guest.scc \
                           cfg/paravirt_kvm.scc \
"
