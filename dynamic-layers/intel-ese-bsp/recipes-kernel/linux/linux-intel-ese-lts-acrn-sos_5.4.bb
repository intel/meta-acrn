require linux-intel-ese-acrn.inc

SRC_URI:append = " ${@bb.utils.contains('ACRN_BOARD', 'ehl-crb-b', bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', 'file://sos_5.4_hybrid_rt_fusa.scc', '', d), '', d)} \
                   file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-ese-lts-acrn-sos"

SUMMARY = "Linux Intel ESE Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES:append = " features/criu/criu-enable.scc \
                           features/docker/docker.scc \
                           cgl/cfg/iscsi.scc \
                           ${@bb.utils.contains('ACRN_BOARD', 'ehl-crb-b', bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', 'sos_5.4_hybrid_rt_fusa.scc', '', d), '', d)} \
                           sos_5.4.scc \
"

