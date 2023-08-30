SUMMARY = "Linux Kernel 5.19 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/mainline-tracking.git;protocol=https;name=machine;nobranch=1; \
                    "

KMETA_BRANCH = "yocto-5.19"

LINUX_VERSION ?= "5.19.0"
SRCREV_machine ?= "e999ad3f8ef0a7bcc2ce051cd28085280ce9a9c0"
SRCREV_meta ?= "f5d4c109d6de04005def04c3a06f053ae0c397ad"
