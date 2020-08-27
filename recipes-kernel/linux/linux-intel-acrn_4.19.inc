SUMMARY = "Linux Kernel 4.19 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI_append = "  file://perf-fix-build-with-binutils.patch \
                    file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
"

KBRANCH = "4.19/base"
KMETA_BRANCH = "yocto-4.19"

LINUX_VERSION ?= "4.19.140"
SRCREV_machine ?= "5050af27abd725b69e5a2867194db6d66e5c8896"
SRCREV_meta ?= "6dd03685eaf5d27cd6e88ee888c0d42e09befd17"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "elfutils-native openssl-native util-linux-native"

KERNEL_FEATURES_append = " features/netfilter/netfilter.scc \
                           features/security/security.scc  \
                           cfg/hv-guest.scc \
                           cfg/paravirt_kvm.scc \
                           features/net/stmicro/stmmac.cfg \
"

# This configuration does not applies to current 4.19 kernel
SRC_URI_remove = "file://enable_lynxpoint_gpio.cfg"
