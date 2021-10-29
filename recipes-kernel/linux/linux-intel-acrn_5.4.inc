SUMMARY = "Linux Kernel 5.4 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KBRANCH = "5.4/yocto"
KMETA_BRANCH = "yocto-5.4"

LINUX_VERSION ?= "5.4.143"
SRCREV_machine ?= "f74600f861eeb3536c7443943a3ce78a77c692dc"
SRCREV_meta ?= "2f0c77d118926b6666062a752a8c9d9a7c07320d"

DEPENDS += "elfutils-native openssl-native util-linux-native"

KERNEL_FEATURES:append = " features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"
# Kernel config 'CONFIG_GPIO_LYNXPOINT' goes by a different name 'CONFIG_PINCTRL_LYNXPOINT' in
# linux-intel. This cause warning during kernel config audit. So suppress the harmless warning for now.
KCONF_BSP_AUDIT_LEVEL = "0"
