SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (UOS)"

require recipes-kernel/linux/linux-intel.inc

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-rt-acrn-uos":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-rt-acrn-uos to enable it")
}

SRC_URI_append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                    file://uos_rt_4.19.scc \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KBRANCH = "4.19/preempt-rt"
KMETA_BRANCH = "yocto-4.19"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION ?= "4.19.165"
SRCREV_machine ?= "4b0ea4d493b0f8fca0979b4351660cc4e01594c1"
SRCREV_meta ?= "147f6b827c6e7766cb2b61da4e17479ffcd216c2"

LINUX_VERSION_EXTENSION = "-linux-intel-preempt-rt-acrn-uos"

LINUX_KERNEL_TYPE = "preempt-rt"

KERNEL_FEATURES_append = "features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"

# This configuration does not applies to current 4.19 rt kernel
SRC_URI_remove = "file://enable_lynxpoint_gpio.cfg"
