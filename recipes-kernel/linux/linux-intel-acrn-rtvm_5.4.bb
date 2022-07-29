SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require recipes-kernel/linux/linux-intel.inc

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-acrn-rtvm":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-acrn-rtvm to enable it")
}

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                    file://user-rtvm_5.4.scc \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KBRANCH = "5.4/preempt-rt"
KMETA_BRANCH = "yocto-5.4"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION ?= "5.4.193"
SRCREV_machine ?= "5776551e21679ed5fec50abbdf4627920b4103b1"
SRCREV_meta ?= "337c38059f2fd562199b0e5133b71410240004e9"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"

KERNEL_FEATURES:append = " features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"

# Kernel config 'CONFIG_GPIO_LYNXPOINT' goes by a different name 'CONFIG_PINCTRL_LYNXPOINT' in
# linux-intel. This cause warning during kernel config audit. So suppress the harmless warning for now.
KCONF_BSP_AUDIT_LEVEL = "0"
