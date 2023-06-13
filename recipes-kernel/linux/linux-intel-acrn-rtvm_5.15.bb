SUMMARY = "Linux Preempt RT Kernel with ACRN enabled (User VM)"

require linux-intel-acrn.inc

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

LINUX_VERSION ?= "5.15.113"
SRCREV_machine ?= "aba6427c7d2c49b8dcfad7d7ec57d0bba719bfb9"
SRCREV_meta ?= "957ddf5f9d4bf5791e88a46ce9ec4352a6d0a171"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"
