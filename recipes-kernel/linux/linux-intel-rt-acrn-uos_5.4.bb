SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (UOS)"

require recipes-kernel/linux/linux-intel.inc

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-rt-acrn-uos":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-rt-acrn-uos to enable it")
}

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                    file://uos_rt_5.4.scc \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KBRANCH = "5.4/preempt-rt"
KMETA_BRANCH = "yocto-5.4"

DEPENDS += "elfutils-native openssl-native util-linux-native"

LINUX_VERSION ?= "5.4.143"
SRCREV_machine ?= "36a2ba81b7c7178b0612363a1af6f22887905cc6"
SRCREV_meta ?= "2f0c77d118926b6666062a752a8c9d9a7c07320d"

LINUX_VERSION_EXTENSION = "-linux-intel-preempt-rt-acrn-uos"

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
