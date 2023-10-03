SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-acrn-rtvm":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-acrn-rtvm to enable it")
}

SRC_URI:append = "  file://user-rtvm_5.15.scc \
                "

KBRANCH = "5.15/preempt-rt"
KMETA_BRANCH = "yocto-5.15"

LINUX_VERSION ?= "5.15.129"
SRCREV_machine ?= "0aa56022fb159e8e577f34e6ef509e1cee4632f4"
SRCREV_meta ?= "c16749e4e0a2f8a903c36d44f7125dd423600c57"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"
