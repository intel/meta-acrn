require recipes-kernel/linux/linux-intel-ese-lts-5.4_git.bb

FILESEXTRAPATHS_prepend := "${LAYERDIR-ese-bsp}/recipes-kernel/linux/linux-config:${LAYERDIR-ese-bsp}/recipes-kernel/linux/files:${LAYERDIR-acrn}/recipes-kernel/linux/files:"

KERNEL_FEATURES_append = "features/netfilter/netfilter.scc \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          "

KERNEL_PACKAGE_NAME = "kernel"
KERNEL_VERSION_SANITY_SKIP = "1"
KCONF_BSP_AUDIT_LEVEL = "0"
