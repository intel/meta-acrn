SUMMARY = "Linux Kernel 5.15 with ACRN enabled"

require recipes-kernel/linux/linux-intel.inc

SRC_URI:append = "  file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch"
SRC_URI:remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI:prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"


LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KBRANCH = "master"
KMETA_BRANCH = "yocto-5.15"


LINUX_VERSION = "5.15.44"
SRCREV_machine = "8ccb82770b88df57e88e407e08e2bda8974dddb7"
SRCREV_meta = "f122fe59e74365eba84bae800898ffd7329c628d"

DEPENDS += "elfutils-native openssl-native util-linux-native"

KERNEL_FEATURES:append = " features/netfilter/netfilter.scc \
                          features/security/security.scc  \
                          cfg/hv-guest.scc \
                          cfg/paravirt_kvm.scc \
                          features/net/stmicro/stmmac.cfg \
"
