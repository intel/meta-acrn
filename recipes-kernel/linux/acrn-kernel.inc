require recipes-kernel/linux/linux-intel_4.19.bb

KBRANCH = "master"

SRC_URI_remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"

# tag: v1.5.1
LINUX_VERSION = "4.19.73"
SRCREV_machine = "0bf3d99ba46b80a87f7b0f2864ba0432e34a9070"

KERNEL_EXTRA_FEATURES += " cfg/hv-guest.scc  cfg/paravirt_kvm.scc "
