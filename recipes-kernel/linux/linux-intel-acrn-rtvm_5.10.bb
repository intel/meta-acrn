SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require recipes-kernel/linux/linux-intel.inc

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-acrn-rtvm":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-acrn-rtvm to enable it")
}

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                    file://user-rtvm_5.10.scc \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KBRANCH = "5.10/preempt-rt"
KMETA_BRANCH = "yocto-5.10"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION ?= "5.10.140"
SRCREV_machine ?= "3d13c043116235dbef38c6fc36599a8d712e36a7"
SRCREV_meta ?= "dcf3fae266930708f432bf37ed4668a1ea9c5ac4"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"

KERNEL_FEATURES:append = " features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"
# Following commit is backported from mainline 5.12-rc to linux-intel 5.10 kernel
# Commit: https://github.com/torvalds/linux/commit/26499e0518a77de29e7db2c53fb0d0e9e15be8fb
# In which 'CONFIG_DRM_GMA3600' config option is dropped.
# This causes warning during config audit. So suppress the harmless warning for now.
KCONF_BSP_AUDIT_LEVEL = "0"
