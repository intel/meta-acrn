SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require recipes-kernel/linux/linux-intel.inc

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-acrn-rtvm":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-acrn-rtvm to enable it")
}

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                    file://user-rtvm_5.15.scc \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KBRANCH = "5.15/preempt-rt"
KMETA_BRANCH = "yocto-5.15"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION ?= "5.15.49"
SRCREV_machine ?= "56b60d58176fd42978fa3418a962fe4a038d164f"
SRCREV_meta ?= "d9823ca27110545274f77718aefbd809c29947e6"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"

KERNEL_FEATURES:append = " features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"
# Following commit is backported from mainline 5.19 to linux-intel 5.15 kernel
# Commit: https://github.com/torvalds/linux/commit/8b766b0f8eece55155146f7628610ce54a065e0f
# In which 'CONFIG_FB_BOOT_VESA_SUPPORT' config option is dropped.
# This causes warning during config audit. So suppress the harmless warning for now.
KCONF_BSP_AUDIT_LEVEL = "0"
