SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    file://0001-efi-libstub-Use-std-gnu11-to-fix-build-with-GCC-15.patch \
                    file://0002-x86-boot-Use-std-gnu11-to-fix-build-with-GCC-15.patch \
                    file://0003-x86-boot-Compile-boot-code-with-std-gnu11-too.patch \
                    "

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-acrn-rtvm":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-acrn-rtvm to enable it")
}

SRC_URI:append = "  file://user-rtvm_6.1.scc \
                "

KBRANCH = "6.1/preempt-rt"
KMETA_BRANCH = "yocto-6.1"

LINUX_VERSION ?= "6.1.120"
SRCREV_machine ?= "4e71d19579a2d25d2aa2f0840c3434de41b84d29"
SRCREV_meta ?= "6eaf8a4970cbb152ff4c6403a7cf8a14e540be1b"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"
