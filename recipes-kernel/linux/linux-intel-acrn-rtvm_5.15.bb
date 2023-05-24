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

LINUX_VERSION ?= "5.15.71"
SRCREV_machine ?= "e29405e36bfbda7ace776548de802b76f61b80d9"
SRCREV_meta ?= "7b8c11231180c913824a3ca227f111ce1a7efb1d"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-preempt-rtvm"

LINUX_KERNEL_TYPE = "preempt-rt"
