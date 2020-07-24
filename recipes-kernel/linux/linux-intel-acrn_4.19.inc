SUMMARY = "Linux Kernel 4.19 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI_append = "  file://perf-fix-build-with-binutils.patch \
                    file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
"

KBRANCH = "4.19/base"
KMETA_BRANCH = "yocto-4.19"

LINUX_VERSION ?= "4.19.127"
SRCREV_machine ?= "5fb01362d28430df1fc12b09898510f0e78812be"
SRCREV_meta ?= "7cb520d405cd5ca8f21a333941fbc0861bbb36b0"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "elfutils-native openssl-native util-linux-native"

KERNEL_FEATURES_append = " features/netfilter/netfilter.scc \
                           features/security/security.scc  \
                           cfg/hv-guest.scc \
                           cfg/paravirt_kvm.scc \
                           features/net/stmicro/stmmac.cfg \
"
