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
SRCREV_machine ?= "200a2dbc2fdf6100f19a59c2d92174d604cf0812"
SRCREV_meta ?= "f5d4c109d6de04005def04c3a06f053ae0c397ad"

LINUX_VERSION_EXTENSION = "-mainline-tracking-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"
