require linux-intel-acrn_5.4.inc

SRC_URI_append = " ${@bb.utils.contains('ACRN_BOARD', \
                    'ehl-crb-b', bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', 'file://sos_5.4_hybrid_rt_fusa.scc', '', d), \
                    '', d)} \
                   file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES_append = "features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
                          ${@bb.utils.contains('ACRN_BOARD', 'ehl-crb-b', \
                            bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', 'sos_5.4_hybrid_rt_fusa.scc', '', d), '', d)} \
                          sos_5.4.scc \
"

# sos_5.4.scc override CONFIG_REFCOUNT_FULL set in security.scc is causing warning during
# config audit. Suppress this harmless warning.
KCONF_AUDIT_LEVEL = "0"
