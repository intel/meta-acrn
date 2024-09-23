SUMMARY = "Linux Kernel 6.1 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "
KBRANCH = "6.1/linux"
KMETA_BRANCH = "yocto-6.1"

LINUX_VERSION ?= "6.1.105"
SRCREV_machine ?= "5404e6dd8524e9cc698099c8780a6889e24ecbfd"
SRCREV_meta ?= "6eaf8a4970cbb152ff4c6403a7cf8a14e540be1b"
