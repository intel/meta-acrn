SUMMARY = "Linux Kernel 5.10 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KBRANCH = "5.10/yocto"
KMETA_BRANCH = "yocto-5.10"

LINUX_VERSION ?= "5.10.109"
SRCREV_machine ?= "6410aba3b60feba47b9713f297d9b3bdb79981f5"
SRCREV_meta ?= "19e7547dd6617760d6094b7a42da1a718b5a96ee"

DEPENDS += "elfutils-native openssl-native util-linux-native"

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
