SUMMARY = "Linux Kernel 5.15 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "

KBRANCH = "5.15/linux"
KMETA_BRANCH = "yocto-5.15"

LINUX_VERSION ?= "5.15.137"
SRCREV_machine ?= "b2769cf869322589ab9147c774404f1f62b6561d"
SRCREV_meta ?= "328b31a095c93537e53e4581cb0b8b0433bfa40c"
