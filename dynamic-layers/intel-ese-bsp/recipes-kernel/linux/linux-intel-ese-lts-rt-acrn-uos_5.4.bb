require recipes-kernel/linux/linux-intel-ese-lts-rt-5.4_git.bb

FILESEXTRAPATHS_prepend := "${LAYERDIR-ese-bsp}/recipes-kernel/linux/linux-config:${LAYERDIR-ese-bsp}/recipes-kernel/linux/files:${LAYERDIR-acrn}/recipes-kernel/linux/files:"

SRC_URI_append = "  file://uos_rt_5.4.scc"

KERNEL_FEATURES_append = "features/netfilter/netfilter.scc \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"

LINUX_VERSION_EXTENSION = "-linux-intel-ese-lts-preempt-rt-acrn-uos"

SUMMARY = "Linux Intel ESE Preempt RT Kernel with ACRN enabled (UOS)"

KERNEL_PACKAGE_NAME = "kernel"
KERNEL_VERSION_SANITY_SKIP = "1"
KCONF_BSP_AUDIT_LEVEL = "0"
