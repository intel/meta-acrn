require linux-intel-acrn_5.4.inc

SRC_URI_append = "  file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES_append = "features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
                          sos_5.4.scc \
"

# sos_5.4.scc override CONFIG_REFCOUNT_FULL set in security.scc is causing warning during
# config audit. Suppress this harmless warning.
KCONF_AUDIT_LEVEL = "0"
