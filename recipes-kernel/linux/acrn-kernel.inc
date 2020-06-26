require linux-intel-acrn_4.19.inc

KBRANCH = "release_1.6"

SRC_URI_remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"

# tag: v1.6
LINUX_VERSION = "4.19.106"
SRCREV_machine = "33161e280f2bd056175ae608581d86d27c862fc1"
