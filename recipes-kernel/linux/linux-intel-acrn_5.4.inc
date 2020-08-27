SUMMARY = "Linux Kernel 5.4 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI_append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KBRANCH = "5.4/yocto"
KMETA_BRANCH = "yocto-5.4"

LINUX_VERSION ?= "5.4.58"
SRCREV_machine ?= "b8db9b1980fb608c295240bb2748955d178c91fd"
SRCREV_meta ?= "7ed307f65171d331a784ceecb6f56b9f8ecadb10"

DEPENDS += "elfutils-native openssl-native util-linux-native"

KERNEL_FEATURES_append = "features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"
# Kernel config 'CONFIG_GPIO_LYNXPOINT' goes by a different name 'CONFIG_PINCTRL_LYNXPOINT' in
# linux-intel. This cause warning during kernel config audit. So suppress the harmless warning for now.
KCONF_BSP_AUDIT_LEVEL = "0"
