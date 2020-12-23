SUMMARY = "Linux Kernel 4.19 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI_append = " file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
"

KBRANCH = "4.19/base"
KMETA_BRANCH = "yocto-4.19"

LINUX_VERSION ?= "4.19.163"
SRCREV_machine ?= "e96097be885e02da48a972a0b0d2dd27d8e00e57"
SRCREV_meta ?= "6dd03685eaf5d27cd6e88ee888c0d42e09befd17"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "elfutils-native openssl-native util-linux-native"

KERNEL_FEATURES_append = " features/netfilter/netfilter.scc \
                           features/security/security.scc  \
                           cfg/hv-guest.scc \
                           cfg/paravirt_kvm.scc \
                           features/net/stmicro/stmmac.cfg \
"
