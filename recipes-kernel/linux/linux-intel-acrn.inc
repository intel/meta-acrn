SUMMARY = "Linux Intel Kernel with ACRN enabled"

require recipes-kernel/linux/linux-yocto.inc
require recipes-kernel/linux/meta-intel-compat-kernel.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KERNEL_CONFIG_URI ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${KMETA_BRANCH};destsuffix=${KMETA}"

SRC_URI = " \
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
