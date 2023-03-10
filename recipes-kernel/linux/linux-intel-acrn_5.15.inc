SUMMARY = "Linux Kernel 5.15 with ACRN enabled"

require linux-intel-acrn.inc

KBRANCH = "5.15/linux"
KMETA_BRANCH = "yocto-5.15"

LINUX_VERSION ?= "5.15.71"
SRCREV_machine ?= "d47a37762969ea0d354a73e568a116eed6530d3c"
SRCREV_meta ?= "7b8c11231180c913824a3ca227f111ce1a7efb1d"
