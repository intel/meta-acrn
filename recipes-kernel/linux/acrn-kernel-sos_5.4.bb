require acrn-kernel_5.4.inc
# sos_5.4.scc override CONFIG_REFCOUNT_FULL set in security.scc is causing warning during
# config audit. Suppress this harmless warning.
KCONF_AUDIT_LEVEL = "0"

SRC_URI_append = "  file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-sos"

SUMMARY = "ACRN Kernel (SOS)"

KERNEL_FEATURES_append = " sos_5.4.scc "
