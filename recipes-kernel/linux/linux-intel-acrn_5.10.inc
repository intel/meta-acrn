SUMMARY = "Linux Kernel 5.10 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                    file://0001-regulator-consumer-Add-missing-stubs-to-regulator-co.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KBRANCH = "5.10/yocto"
KMETA_BRANCH = "yocto-5.10"

LINUX_VERSION ?= "5.10.115"
SRCREV_machine ?= "6d2c1213aa7e48623e06ec2243a390bfb2f86ecd"
SRCREV_meta ?= "6337d56f23d18e5680493dadcb52899d5e6a7c09"

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
