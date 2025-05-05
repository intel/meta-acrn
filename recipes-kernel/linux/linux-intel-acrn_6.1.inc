SUMMARY = "Linux Kernel 6.1 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    file://0001-efi-libstub-Use-std-gnu11-to-fix-build-with-GCC-15.patch \
                    file://0002-x86-boot-Use-std-gnu11-to-fix-build-with-GCC-15.patch \
                    file://0003-x86-boot-Compile-boot-code-with-std-gnu11-too.patch \
                    "
KBRANCH = "6.1/linux"
KMETA_BRANCH = "yocto-6.1"

LINUX_VERSION ?= "6.1.120"
SRCREV_machine ?= "5a2eed202a03376b0c9098bc7b9f06eba1be317f"
SRCREV_meta ?= "6eaf8a4970cbb152ff4c6403a7cf8a14e540be1b"
