require recipes-kernel/linux/linux-intel_5.4.bb

SRC_URI_append = "  file://sos_5.4.scc"

LINUX_VERSION_EXTENSION ?= "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES_append = " cfg/hv-guest.scc \
                           cfg/paravirt_kvm.scc \
                           sos_5.4.scc \
"
