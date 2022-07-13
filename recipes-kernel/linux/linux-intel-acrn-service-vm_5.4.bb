require linux-intel-acrn_5.4.inc

SRC_URI:append = " ${@bb.utils.contains('ACRN_BOARD', \
                    'ehl-crb-b', bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', 'file://service-vm_5.4_hybrid_rt_fusa.scc', '', d), \
                    '', d)} \
                   file://service-vm_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-service-vm"

SUMMARY = "Linux Kernel with ACRN enabled (Service VM)"

KERNEL_FEATURES:append = " features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
                          ${@bb.utils.contains('ACRN_BOARD', 'ehl-crb-b', \
                            bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', 'service-vm_5.4_hybrid_rt_fusa.scc', '', d), '', d)} \
                          service-vm_5.4.scc \
"

# service-vm_5.4.scc override CONFIG_REFCOUNT_FULL set in security.scc is causing warning during
# config audit. Suppress this harmless warning.
KCONF_AUDIT_LEVEL = "0"
