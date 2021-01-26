require linux-intel-acrn_5.4.inc

KBRANCH = "master"

SRC_URI_remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"

LINUX_VERSION = "5.4.73"
SRCREV_machine = "0dad2fa8edcbe288a1737d485959c5800caa47a7"
