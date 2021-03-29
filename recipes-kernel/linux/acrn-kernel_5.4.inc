require linux-intel-acrn_5.4.inc

KBRANCH = "master"

SRC_URI_remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"

LINUX_VERSION = "5.4.90"
SRCREV_machine = "07fb8047e3dc1852881fc6dd6be12f11083d46f1"
