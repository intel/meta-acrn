SUMMARY = "Linux Kernel 5.15 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "

KBRANCH = "5.15/linux"
KMETA_BRANCH = "yocto-5.15"

LINUX_VERSION ?= "5.15.129"
SRCREV_machine ?= "6a3035823c5db6ee250a049f98a6d7bd53f837c7"
SRCREV_meta ?= "c16749e4e0a2f8a903c36d44f7125dd423600c57"
