require linux-intel-ese-acrn.inc

SRC_URI_append = "  file://sos_5.4.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-ese-lts-acrn-sos"

SUMMARY = "Linux Intel ESE Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES_append = " features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
"

SRC_URI_append = " ${@get_scenario_cfg(d)}"

def get_scenario_cfg(d):
    if bb.utils.contains('ACRN_BOARD', 'ehl-crb-b', True, False, d):
        if bb.utils.contains('ACRN_SCENARIO', 'hybrid_rt_fusa', True, False, d):
            return 'file://sos_5.4_hybrid_rt_fusa.scc'
    return ''
