SUMMARY = "Linux Kernel 5.19 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/mainline-tracking.git;protocol=https;name=machine;nobranch=1; \
                    "

KMETA_BRANCH = "yocto-5.19"

LINUX_VERSION ?= "5.19.0"
SRCREV_machine ?= "a436283ed605fe33d4a6ba58470af8e01dd8b0d0"
SRCREV_meta ?= "f5d4c109d6de04005def04c3a06f053ae0c397ad"
