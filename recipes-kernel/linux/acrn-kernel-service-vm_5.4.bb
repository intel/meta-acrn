require acrn-kernel_5.4.inc
# service-os_5.4.scc override CONFIG_REFCOUNT_FULL set in security.scc is causing warning during
# config audit. Suppress this harmless warning.
KCONF_AUDIT_LEVEL = "0"

SRC_URI:append = "  file://service-vm_5.4.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-service-vm"

SUMMARY = "ACRN Kernel (Service VM)"

KERNEL_FEATURES:append = " service-vm_5.4.scc "
