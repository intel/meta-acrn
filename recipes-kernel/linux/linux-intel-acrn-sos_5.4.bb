require linux-intel-acrn_5.4.inc

SRC_URI_append = "  file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES_append = "features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
                          sos_5.4.scc \
"
