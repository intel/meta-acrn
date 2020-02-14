require recipes-kernel/linux/linux-intel_4.19.bb

KBRANCH = "master"

SRC_URI_remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"

# tag: v1.5.1
LINUX_VERSION = "4.19.94"
SRCREV_machine = "3f1f0b99eaae6b1ce9a1f2f9389a0040e19e0e39"

KERNEL_EXTRA_FEATURES += " cfg/hv-guest.scc  cfg/paravirt_kvm.scc "
