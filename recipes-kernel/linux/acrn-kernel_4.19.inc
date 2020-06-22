require linux-intel-acrn_4.19.inc

KBRANCH = "release_1.6"

SRC_URI_remove = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH};"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-kernel.git;protocol=https;name=machine;branch=${KBRANCH};"

SRC_URI_remove = "file://0001-Add-the-plane-restrictionfor-SKL.-Otherwise-there-is.patch"
SRC_URI_remove = "file://0002-Add-the-change-for-gvt-g-on-SKL.patch"

# tag: v1.6
LINUX_VERSION = "4.19.106"
SRCREV_machine = "02751a90703b49423905a7a1030816527f5e9c86"
