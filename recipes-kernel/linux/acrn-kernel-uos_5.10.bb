require acrn-kernel_5.10.inc

SRC_URI:append = "  file://uos_5.10.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-uos"

SUMMARY = "ACRN Kernel (UOS)"

KERNEL_FEATURES:append = " features/module-signing/signing.scc \
                        uos_5.10.scc \
                        "
