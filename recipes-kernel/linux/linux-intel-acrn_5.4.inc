SUMMARY = "Linux Kernel 5.4 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI_append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KBRANCH = "5.4/yocto"
KMETA_BRANCH = "yocto-5.4"

LINUX_VERSION ?= "5.4.81"
SRCREV_machine ?= "b3d20595bdf59d0784cb8256445fff65df26c4ac"
SRCREV_meta ?= "8930d401f840f6cdff4ac887f6f6832d8cd44112"

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
