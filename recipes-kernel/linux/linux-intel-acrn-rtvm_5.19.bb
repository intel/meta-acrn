SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/mainline-tracking.git;protocol=https;name=machine;nobranch=1; \
                    "

# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("KERNEL_PACKAGE_NAME", True) == "kernel" and d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-intel-acrn-rtvm":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-intel-acrn-rtvm to enable it")
}

SRC_URI:append = "  file://user-rtvm_5.15.scc \
                "

KMETA_BRANCH = "yocto-5.19"

LINUX_VERSION ?= "5.19.0"
SRCREV_machine ?= "fa899c4db66b32353d91e0e3c48a6eaf72ff5931"
SRCREV_meta ?= "61d7aaaa97297780205a333d529e55136e20cb11"

LINUX_VERSION_EXTENSION = "-mainline-tracking-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"
