SUMMARY = "Linux Kernel 5.15 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "
SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
                 "

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KBRANCH = "5.15/linux"
KMETA_BRANCH = "yocto-5.15"

LINUX_VERSION ?= "5.15.71"
SRCREV_machine ?= "d47a37762969ea0d354a73e568a116eed6530d3c"
SRCREV_meta ?= "7b8c11231180c913824a3ca227f111ce1a7efb1d"

DEPENDS += "elfutils-native openssl-native util-linux-native"

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
