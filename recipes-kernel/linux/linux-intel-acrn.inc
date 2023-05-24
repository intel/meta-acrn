SUMMARY = "Linux Intel Kernel with ACRN enabled"

require recipes-kernel/linux/linux-yocto.inc
require recipes-kernel/linux/meta-intel-compat-kernel.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KERNEL_CONFIG_URI ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${KMETA_BRANCH};destsuffix=${KMETA}"

SRC_URI = " \
           git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
           ${KERNEL_CONFIG_URI} \
           file://0001-vt-conmakehash-improve-reproducibility.patch \
           file://0001-lib-build_OID_registry-fix-reproducibility-issues.patch \
           file://fix-perf-reproducibility.patch \
           file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
	  "

KMETA = "kernel-meta"

DEPENDS += "elfutils-native openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE ?= "(intel-corei7-64)"

KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"

KERNEL_FEATURES:append = " features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"
# Following commit is backported from mainline 5.19 to linux-intel 5.15 kernel
# Commit: https://github.com/torvalds/linux/commit/8b766b0f8eece55155146f7628610ce54a065e0f
# In which 'CONFIG_FB_BOOT_VESA_SUPPORT' config option is dropped.
# This causes warning during config audit. So suppress the harmless warning for now.
KCONF_BSP_AUDIT_LEVEL = "0"
