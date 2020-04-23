SUMMARY = "ACRN hypervisor"

inherit packagegroup ${@bb.utils.contains('LAYERSERIES_CORENAMES', 'zeus', 'distro_features_check', 'features_check', d)}

# Currently requires systemd as the networking glue is systemd-specific
REQUIRED_DISTRO_FEATURES = "systemd"

RDEPENDS_${PN} = "\
    acrn-hypervisor \
    acrn-tools \
    acrn-devicemodel \
    acrn-efi-setup \
    "
